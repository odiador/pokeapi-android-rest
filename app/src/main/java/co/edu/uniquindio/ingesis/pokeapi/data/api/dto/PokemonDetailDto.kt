package co.edu.uniquindio.ingesis.pokeapi.data.api.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("base_experience") val baseExperience: Int?,
    @SerializedName("sprites") val sprites: SpritesDto,
    @SerializedName("types") val types: List<TypeSlotDto>,
    @SerializedName("abilities") val abilities: List<AbilitySlotDto>,
    @SerializedName("stats") val stats: List<StatSlotDto>
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?
)

data class TypeSlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: NamedResourceDto
)

data class AbilitySlotDto(
    @SerializedName("ability") val ability: NamedResourceDto,
    @SerializedName("is_hidden") val isHidden: Boolean
)

data class StatSlotDto(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("stat") val stat: NamedResourceDto
)
