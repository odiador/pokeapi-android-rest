package co.edu.uniquindio.ingesis.pokeapi.data.repository

import co.edu.uniquindio.ingesis.pokeapi.data.local.dao.PokemonDao
import co.edu.uniquindio.ingesis.pokeapi.data.mapper.toDetailEntity
import co.edu.uniquindio.ingesis.pokeapi.data.mapper.toDomain
import co.edu.uniquindio.ingesis.pokeapi.data.mapper.toEntity
import co.edu.uniquindio.ingesis.pokeapi.data.mapper.toListItemOrNull
import co.edu.uniquindio.ingesis.pokeapi.data.remote.api.PokemonApiService
import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
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

        override suspend fun fetchPokemonPage(
            page: Int,
            pageSize: Int,
        ) {
            val offset = page * pageSize
            val response = api.getPokemonList(offset, pageSize)
            val items = response.results.mapNotNull { it.toListItemOrNull() }
            dao.insertPokemonList(items.map { it.toEntity() })
        }

        override fun observePokemonDetail(id: Int): Flow<Pokemon?> {
            return dao.observePokemonDetail(id).map { it?.toDomain() }
        }

        override suspend fun fetchPokemonDetail(id: Int) {
            val dto = api.getPokemonDetail(id)
            dao.insertPokemonDetail(dto.toDetailEntity())
        }
    }
