package co.edu.uniquindio.ingesis.pokeapi.data.repository

import co.edu.uniquindio.ingesis.pokeapi.data.mock.FakePokemonData
import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePokemonRepository : PokemonRepository {
    private val listState = MutableStateFlow<List<PokemonListItem>>(emptyList())
    private val detailState = MutableStateFlow<Map<Int, Pokemon>>(emptyMap())
    private val allPokemon = FakePokemonData.allPokemon

    override fun observePokemonList(): Flow<List<PokemonListItem>> = listState.asStateFlow()

    override fun observeFilteredPokemonList(
        query: String,
        type: String?,
        limit: Int,
        offset: Int,
    ): Flow<List<PokemonListItem>> {
        return listState.asStateFlow().map { items ->
            items.filter { item ->
                val matchesSearch =
                    query.isBlank() ||
                        item.name.contains(query, ignoreCase = true) ||
                        item.id.toString() == query
                val matchesType = type == null || item.types.contains(type)
                matchesSearch && matchesType
            }.drop(offset).take(limit)
        }
    }

    override suspend fun fetchPokemonPage(
        page: Int,
        pageSize: Int,
    ) {
        val start = page * pageSize
        if (start >= allPokemon.size) return
        val end = (start + pageSize).coerceAtMost(allPokemon.size)
        val pageItems = allPokemon.subList(start, end)

        listState.update { current ->
            (current + pageItems.map { it.toListItem() }).distinctBy { it.id }
        }
        detailState.update { current ->
            val updated = current.toMutableMap()
            pageItems.forEach { updated[it.id] = it }
            updated
        }
    }

    override fun observePokemonDetail(id: Int): Flow<Pokemon?> {
        return detailState.asStateFlow().map { it[id] }
    }

    override suspend fun fetchPokemonDetail(id: Int) {
        val pokemon = allPokemon.firstOrNull { it.id == id } ?: return
        detailState.update { current ->
            if (current.containsKey(id)) {
                current
            } else {
                current + (id to pokemon)
            }
        }
    }

    override suspend fun searchPokemon(name: String): Pokemon? {
        return allPokemon.find { it.name.equals(name, ignoreCase = true) }
    }

    override suspend fun fetchPokemonsByType(typeName: String) {
        // No-op for now in fake
    }

    override suspend fun getAvailableTypes(): List<String> {
        return listOf("grass", "fire", "water")
    }

    private fun Pokemon.toListItem(): PokemonListItem =
        PokemonListItem(
            id = id,
            name = name,
            imageUrl = imageUrl,
        )
}
