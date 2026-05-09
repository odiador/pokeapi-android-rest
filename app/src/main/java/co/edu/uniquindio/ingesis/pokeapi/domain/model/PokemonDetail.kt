package co.edu.uniquindio.ingesis.pokeapi.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int?,
    val spriteUrl: String?,
    val types: List<String>,
    val abilities: List<PokemonAbility>,
    val stats: List<PokemonStat>,
)

data class PokemonAbility(
    val name: String,
    val isHidden: Boolean,
)

data class PokemonStat(
    val name: String,
    val value: Int,
)
