package co.edu.uniquindio.ingesis.pokeapi.data.remote.dto

data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: SpritesDto,
    val types: List<TypeSlotDto>,
)
