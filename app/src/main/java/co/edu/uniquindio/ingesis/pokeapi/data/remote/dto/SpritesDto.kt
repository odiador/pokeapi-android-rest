package co.edu.uniquindio.ingesis.pokeapi.data.remote.dto

import com.squareup.moshi.Json

data class SpritesDto(
    @Json(name = "front_default") val frontDefault: String?,
)
