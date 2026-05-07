package co.edu.uniquindio.ingesis.pokeapi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.edu.uniquindio.ingesis.pokeapi.ui.screens.detail.PokemonDetailScreen
import co.edu.uniquindio.ingesis.pokeapi.ui.screens.list.PokemonListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route
    ) {
        composable(Screen.PokemonList.route) {
            PokemonListScreen(
                onNavigateToDetail = { pokemonId ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                }
            )
        }
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.StringType })
        ) {
            PokemonDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
