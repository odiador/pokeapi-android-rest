package co.edu.uniquindio.ingesis.pokeapi.data.api.dto

import com.google.gson.annotations.SerializedName

data class NamedResourceDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
