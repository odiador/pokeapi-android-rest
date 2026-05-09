package co.edu.uniquindio.ingesis.pokeapi.domain.repository

import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun observePokemonList(): Flow<List<PokemonListItem>>

    fun observeFilteredPokemonList(
        query: String,
        type: String?,
        limit: Int = 1000,
        offset: Int = 0,
    ): Flow<List<PokemonListItem>>

    suspend fun fetchPokemonPage(
        page: Int,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    )

    fun observePokemonDetail(id: Int): Flow<Pokemon?>

    suspend fun fetchPokemonDetail(id: Int)

    suspend fun searchPokemon(name: String): Pokemon?

    suspend fun fetchPokemonsByType(typeName: String)

    suspend fun getAvailableTypes(): List<String>

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
