package co.edu.uniquindio.ingesis.pokeapi.data.api.dto

import com.google.gson.annotations.SerializedName

data class TypeListDto(
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<NamedResourceDto>
)
