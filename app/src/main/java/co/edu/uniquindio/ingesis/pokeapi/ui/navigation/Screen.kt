package co.edu.uniquindio.ingesis.pokeapi.ui.navigation

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: String) = "pokemon_detail/$pokemonId"
    }
}
