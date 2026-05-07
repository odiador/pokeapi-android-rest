package co.edu.uniquindio.ingesis.pokeapi.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Private file-level helpers for JSON serialization
private data class AbilityJson(val name: String, val isHidden: Boolean)
private data class StatJson(val name: String, val value: Int)
import co.edu.uniquindio.ingesis.pokeapi.data.api.PokeApiService
import co.edu.uniquindio.ingesis.pokeapi.data.local.dao.PokemonCacheDao
import co.edu.uniquindio.ingesis.pokeapi.data.local.dao.PokemonDetailDao
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonCacheEntity
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonDetailEntity
import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonAbility
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonDetail
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonStat
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import co.edu.uniquindio.ingesis.pokeapi.network.NetworkStatusMonitor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApiService,
    private val cacheDao: PokemonCacheDao,
    private val detailDao: PokemonDetailDao,
    private val networkMonitor: NetworkStatusMonitor,
    private val gson: Gson
) : PokemonRepository {

    override suspend fun getPokemonPage(page: Int, limit: Int): List<Pokemon> {
        return if (networkMonitor.isCurrentlyOnline()) {
            try {
                val offset = page * limit
                val response = api.getPokemonList(limit = limit, offset = offset)
                val pokemonList = response.results.map { dto ->
                    val id = extractIdFromUrl(dto.url)
                    Pokemon(
                        id = id,
                        name = dto.name,
                        spriteUrl = buildSpriteUrl(id)
                    )
                }
                // Save to cache
                cacheDao.insertAll(pokemonList.map { p ->
                    PokemonCacheEntity(id = p.id, name = p.name, spriteUrl = p.spriteUrl)
                })
                pokemonList
            } catch (e: Exception) {
                loadFromCache()
            }
        } else {
            loadFromCache()
        }
    }

    override suspend fun getPokemonDetail(nameOrId: String): PokemonDetail {
        return if (networkMonitor.isCurrentlyOnline()) {
            try {
                val dto = api.getPokemonDetail(nameOrId)
                val detail = PokemonDetail(
                    id = dto.id,
                    name = dto.name,
                    height = dto.height,
                    weight = dto.weight,
                    baseExperience = dto.baseExperience,
                    spriteUrl = dto.sprites.frontDefault,
                    types = dto.types.sortedBy { it.slot }.map { it.type.name },
                    abilities = dto.abilities.map { PokemonAbility(it.ability.name, it.isHidden) },
                    stats = dto.stats.map { PokemonStat(it.stat.name, it.baseStat) }
                )
                // Save to Room
                detailDao.insert(detail.toEntity())
                detail
            } catch (e: Exception) {
                loadDetailFromCache(nameOrId) ?: throw e
            }
        } else {
            loadDetailFromCache(nameOrId) ?: throw Exception("Sin conexion y sin datos guardados para este Pokemon")
        }
    }

    override suspend fun getTypes(): List<String> {
        return if (networkMonitor.isCurrentlyOnline()) {
            try {
                val response = api.getTypeList()
                // Filter out "unknown" and "shadow" which are not real battle types
                response.results.map { it.name }.filter { it != "unknown" && it != "shadow" }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun getPokemonByType(typeName: String): List<Pokemon> {
        return if (networkMonitor.isCurrentlyOnline()) {
            try {
                val response = api.getTypeDetail(typeName)
                val pokemonList = response.pokemon.take(60).map { slot ->
                    val id = extractIdFromUrl(slot.pokemon.url)
                    Pokemon(
                        id = id,
                        name = slot.pokemon.name,
                        spriteUrl = buildSpriteUrl(id)
                    )
                }
                // Cache results
                cacheDao.insertAll(pokemonList.map { p ->
                    PokemonCacheEntity(id = p.id, name = p.name, spriteUrl = p.spriteUrl)
                })
                pokemonList
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun searchPokemon(name: String): Pokemon? {
        return if (networkMonitor.isCurrentlyOnline()) {
            try {
                val dto = api.getPokemonDetail(name)
                val pokemon = Pokemon(
                    id = dto.id,
                    name = dto.name,
                    spriteUrl = dto.sprites.frontDefault ?: buildSpriteUrl(dto.id)
                )
                // Cache it
                cacheDao.insertAll(listOf(PokemonCacheEntity(pokemon.id, pokemon.name, pokemon.spriteUrl)))
                pokemon
            } catch (e: Exception) {
                // Also try local cache
                cacheDao.getByName(name)?.let { entity ->
                    Pokemon(entity.id, entity.name, entity.spriteUrl)
                }
            }
        } else {
            cacheDao.getByName(name)?.let { entity ->
                Pokemon(entity.id, entity.name, entity.spriteUrl)
            }
        }
    }

    private suspend fun loadFromCache(): List<Pokemon> {
        return cacheDao.getAll().map { entity ->
            Pokemon(entity.id, entity.name, entity.spriteUrl)
        }
    }

    private suspend fun loadDetailFromCache(nameOrId: String): PokemonDetail? {
        val entity = nameOrId.toIntOrNull()?.let { detailDao.getById(it) }
            ?: detailDao.getByName(nameOrId)
        return entity?.toDomain(gson)
    }

    private fun extractIdFromUrl(url: String): Int {
        // URL format: https://pokeapi.co/api/v2/pokemon/1/
        return url.trimEnd('/').split('/').last().toIntOrNull() ?: 0
    }

    private fun buildSpriteUrl(id: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }

    private fun PokemonDetail.toEntity(): PokemonDetailEntity {
        return PokemonDetailEntity(
            id = id,
            name = name,
            height = height,
            weight = weight,
            baseExperience = baseExperience,
            spriteUrl = spriteUrl,
            typesJson = gson.toJson(types),
            abilitiesJson = gson.toJson(abilities.map { AbilityJson(it.name, it.isHidden) }),
            statsJson = gson.toJson(stats.map { StatJson(it.name, it.value) })
        )
    }

    private fun PokemonDetailEntity.toDomain(gson: Gson): PokemonDetail {
        val typesList: List<String> = gson.fromJson(typesJson, object : TypeToken<List<String>>() {}.type)
        val abilitiesList: List<AbilityJson> = gson.fromJson(abilitiesJson, object : TypeToken<List<AbilityJson>>() {}.type)
        val statsList: List<StatJson> = gson.fromJson(statsJson, object : TypeToken<List<StatJson>>() {}.type)
        return PokemonDetail(
            id = id,
            name = name,
            height = height,
            weight = weight,
            baseExperience = baseExperience,
            spriteUrl = spriteUrl,
            types = typesList,
            abilities = abilitiesList.map { PokemonAbility(it.name, it.isHidden) },
            stats = statsList.map { PokemonStat(it.name, it.value) }
        )
    }
}
