package co.edu.uniquindio.ingesis.pokeapi.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PokeRed,
    secondary = PokeYellow,
    background = PokeLightGray,
    surface = PokeLightGray,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = PokeDarkGray,
    onBackground = PokeDarkGray,
    onSurface = PokeDarkGray
)

private val DarkColorScheme = darkColorScheme(
    primary = PokeRed,
    secondary = PokeYellow,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = PokeDarkGray
)

@Composable
fun PokeapiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
