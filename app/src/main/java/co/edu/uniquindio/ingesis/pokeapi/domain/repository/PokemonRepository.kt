package co.edu.uniquindio.ingesis.pokeapi.domain.repository

import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonDetail

interface PokemonRepository {
    suspend fun getPokemonPage(page: Int, limit: Int = 20): List<Pokemon>
    suspend fun getPokemonDetail(nameOrId: String): PokemonDetail
    suspend fun getTypes(): List<String>
    suspend fun getPokemonByType(typeName: String): List<Pokemon>
    suspend fun searchPokemon(name: String): Pokemon?
}
