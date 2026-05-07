package co.edu.uniquindio.ingesis.pokeapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.edu.uniquindio.ingesis.pokeapi.ui.navigation.AppNavigation
import co.edu.uniquindio.ingesis.pokeapi.ui.theme.PokeapiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeapiTheme {
                AppNavigation()
            }
        }
    }
}
