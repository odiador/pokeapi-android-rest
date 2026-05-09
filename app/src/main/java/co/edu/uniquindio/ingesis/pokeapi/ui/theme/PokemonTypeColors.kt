package co.edu.uniquindio.ingesis.pokeapi.ui.theme

import androidx.compose.ui.graphics.Color

object PokemonTypeColors {
    val Bug = Color(0xFFA8B820)
    val Dark = Color(0xFF705848)
    val Dragon = Color(0xFF7038F8)
    val Electric = Color(0xFFF8D030)
    val Fairy = Color(0xFFEE99AC)
    val Fighting = Color(0xFFC03028)
    val Fire = Color(0xFFF08030)
    val Flying = Color(0xFFA890F0)
    val Ghost = Color(0xFF705898)
    val Grass = Color(0xFF78C850)
    val Ground = Color(0xFFE0C068)
    val Ice = Color(0xFF98D8D8)
    val Normal = Color(0xFFA8A878)
    val Poison = Color(0xFFA040A0)
    val Psychic = Color(0xFFF85888)
    val Rock = Color(0xFFB8A038)
    val Steel = Color(0xFFB8B8D0)
    val Water = Color(0xFF6890F0)
    val Unknown = Color(0xFF68A090)

    private val typeColorMap =
        mapOf(
            "bug" to Bug,
            "dark" to Dark,
            "dragon" to Dragon,
            "electric" to Electric,
            "fairy" to Fairy,
            "fighting" to Fighting,
            "fire" to Fire,
            "flying" to Flying,
            "ghost" to Ghost,
            "grass" to Grass,
            "ground" to Ground,
            "ice" to Ice,
            "normal" to Normal,
            "poison" to Poison,
            "psychic" to Psychic,
            "rock" to Rock,
            "steel" to Steel,
            "water" to Water,
        )

    fun getColor(type: String): Color {
        return typeColorMap[type.lowercase()] ?: Unknown
    }
}
