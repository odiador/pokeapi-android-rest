package co.edu.uniquindio.ingesis.pokeapi.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import co.edu.uniquindio.ingesis.pokeapi.ui.navigation.appNavHost
import co.edu.uniquindio.ingesis.pokeapi.ui.theme.pokeapiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            pokeapiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    appNavHost()
                }
            }
        }
    }
}
