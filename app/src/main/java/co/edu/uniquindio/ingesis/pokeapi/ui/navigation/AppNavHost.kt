package co.edu.uniquindio.ingesis.pokeapi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.edu.uniquindio.ingesis.pokeapi.ui.screens.detail.pokemonDetailScreen
import co.edu.uniquindio.ingesis.pokeapi.ui.screens.list.pokemonListScreen

@Composable
fun appNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIST,
    ) {
        composable(Routes.LIST) {
            pokemonListScreen(
                onPokemonClick = { id -> navController.navigate("${Routes.DETAIL}/$id") },
            )
        }
        composable(
            route = "${Routes.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            pokemonDetailScreen(
                pokemonId = id,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

private object Routes {
    const val LIST = "list"
    const val DETAIL = "detail"
}
