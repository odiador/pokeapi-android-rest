package co.edu.uniquindio.ingesis.pokeapi.data.api.dto

import com.google.gson.annotations.SerializedName

data class TypeDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("pokemon") val pokemon: List<TypePokemonSlotDto>
)

data class TypePokemonSlotDto(
    @SerializedName("pokemon") val pokemon: NamedResourceDto,
    @SerializedName("slot") val slot: Int
)
