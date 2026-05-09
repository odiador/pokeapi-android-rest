package co.edu.uniquindio.ingesis.pokeapi.domain.model

data class PokemonListItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String> = emptyList(),
)
