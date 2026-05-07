package co.edu.uniquindio.ingesis.pokeapi.data.api

import co.edu.uniquindio.ingesis.pokeapi.data.api.dto.PokemonDetailDto
import co.edu.uniquindio.ingesis.pokeapi.data.api.dto.PokemonListDto
import co.edu.uniquindio.ingesis.pokeapi.data.api.dto.TypeDetailDto
import co.edu.uniquindio.ingesis.pokeapi.data.api.dto.TypeListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    // Endpoint 1: paginated list of pokemon
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListDto

    // Endpoint 2: full detail of a single pokemon
    @GET("pokemon/{nameOrId}")
    suspend fun getPokemonDetail(
        @Path("nameOrId") nameOrId: String
    ): PokemonDetailDto

    // Endpoint 3: list all types (for filter dropdown)
    @GET("type")
    suspend fun getTypeList(
        @Query("limit") limit: Int = 100
    ): TypeListDto

    // Endpoint 4: all pokemon of a given type (type filter)
    @GET("type/{name}")
    suspend fun getTypeDetail(
        @Path("name") name: String
    ): TypeDetailDto
}
