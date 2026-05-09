package co.edu.uniquindio.ingesis.pokeapi.data.remote.dto

import com.squareup.moshi.Json

data class PokemonSpeciesDto(
    val id: Int,
    val name: String,
    @Json(name = "flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntryDto>,
)

data class FlavorTextEntryDto(
    @Json(name = "flavor_text")
    val flavorText: String,
    val language: NamedResourceDto,
)
