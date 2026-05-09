package co.edu.uniquindio.ingesis.pokeapi.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val height: Int,
    val weight: Int,
    val description: String = "",
)
