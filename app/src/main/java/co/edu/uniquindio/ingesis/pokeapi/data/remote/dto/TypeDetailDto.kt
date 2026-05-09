package co.edu.uniquindio.ingesis.pokeapi.data.remote.dto

data class TypeDetailDto(
    val id: Int,
    val name: String,
    val pokemon: List<TypePokemonDto>,
)

data class TypePokemonDto(
    val pokemon: NamedResourceDto,
)
