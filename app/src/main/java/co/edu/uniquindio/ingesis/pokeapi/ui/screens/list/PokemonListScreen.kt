package co.edu.uniquindio.ingesis.pokeapi.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun pokemonListScreen(onPokemonClick: (Int) -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Text(text = "Pokemon list placeholder")
        Button(onClick = { onPokemonClick(1) }) {
            Text(text = "Open Pokemon #1")
        }
    }
}
