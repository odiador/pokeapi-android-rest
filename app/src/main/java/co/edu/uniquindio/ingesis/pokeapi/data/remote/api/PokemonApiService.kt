package co.edu.uniquindio.ingesis.pokeapi.data.remote.api

import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.PokemonDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.PokemonListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): PokemonListResponseDto

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int,
    ): PokemonDto
}
