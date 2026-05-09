package co.edu.uniquindio.ingesis.pokeapi.data.repository

import co.edu.uniquindio.ingesis.pokeapi.data.local.dao.PokemonDao
import co.edu.uniquindio.ingesis.pokeapi.data.mapper.toDetailEntity
import co.edu.uniquindio.ingesis.pokeapi.data.mapper.toDomain
import co.edu.uniquindio.ingesis.pokeapi.data.mapper.toEntity
import co.edu.uniquindio.ingesis.pokeapi.data.remote.api.PokemonApiService
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.PokemonSpeciesDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.TypeDetailDto
import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonRepositoryImpl
    @Inject
    constructor(
        private val api: PokemonApiService,
        private val dao: PokemonDao,
    ) : PokemonRepository {
        override fun observePokemonList(): Flow<List<PokemonListItem>> {
            return dao.observePokemonList().map { items ->
                items.map { it.toDomain() }
            }
        }

        override fun observeFilteredPokemonList(
            query: String,
            type: String?,
            limit: Int,
            offset: Int,
        ): Flow<List<PokemonListItem>> {
            return dao.observeFilteredPokemonList(query, type, limit, offset).map { items ->
                items.map { it.toDomain() }
            }
        }

        override suspend fun fetchPokemonPage(
            page: Int,
            pageSize: Int,
        ) {
            val offset = page * pageSize
            val response = api.getPokemonList(offset, pageSize)

            // Fetch details for each pokemon in parallel to get types
            val entities =
                coroutineScope {
                    response.results.mapNotNull { resource ->
                        val id = resource.url?.trimEnd('/')?.substringAfterLast('/')?.toIntOrNull()
                        id?.let { async { runCatching { api.getPokemonDetail(it) }.getOrNull() } }
                    }.mapNotNull { it.await()?.toEntity() }
                }

            dao.insertPokemonList(entities)
        }

        override fun observePokemonDetail(id: Int): Flow<Pokemon?> {
            return dao.observePokemonDetail(id).map { it?.toDomain() }
        }

        override suspend fun fetchPokemonDetail(id: Int) {
            val dto = api.getPokemonDetail(id)
            val speciesDto: PokemonSpeciesDto? = runCatching { api.getPokemonSpecies(id) }.getOrNull()
            val description =
                speciesDto?.flavorTextEntries
                    ?.find { entry -> entry.language.name == "es" }?.flavorText
                    ?: speciesDto?.flavorTextEntries?.firstOrNull()?.flavorText
                    ?: ""

            val detailEntity =
                dto.toDetailEntity().copy(
                    description = description.replace("\n", " "),
                )
            dao.insertPokemonDetail(detailEntity)
        }

        override suspend fun searchPokemon(name: String): Pokemon? {
            return runCatching {
                val dto = api.getPokemonByName(name.lowercase())
                val pokemon = dto.toDomain()
                // Also add to list if not present
                dao.insertPokemonList(listOf(dto.toEntity()))
                pokemon
            }.getOrNull()
        }

        override suspend fun fetchPokemonsByType(typeName: String) {
            val typeDetail: TypeDetailDto = api.getTypeDetail(typeName.lowercase())

            // Limit to first 40 to avoid massive network surge
            val pokemonRefs = typeDetail.pokemon.take(40)

            val entities =
                coroutineScope {
                    pokemonRefs.map { ref ->
                        val id = ref.pokemon.url?.trimEnd('/')?.substringAfterLast('/')?.toIntOrNull()
                        async { id?.let { runCatching { api.getPokemonDetail(it) }.getOrNull() } }
                    }.mapNotNull { it.await()?.toEntity() }
                }

            dao.insertPokemonList(entities)
        }

        override suspend fun getAvailableTypes(): List<String> {
            return runCatching {
                val types = api.getTypeNames().results.map { it.name }
                if (types.isNotEmpty()) {
                    dao.insertTypes(
                        types.map {
                            co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonTypeEntity(it)
                        },
                    )
                }
                types
            }.getOrElse {
                dao.getAllTypes()
            }
        }
    }
