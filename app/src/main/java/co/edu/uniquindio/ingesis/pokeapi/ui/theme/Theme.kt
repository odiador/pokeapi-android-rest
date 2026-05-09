package co.edu.uniquindio.ingesis.pokeapi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors =
    lightColorScheme(
        primary = PrimaryLight,
        onPrimary = OnPrimaryLight,
        secondary = SecondaryLight,
        onSecondary = OnSecondaryLight,
    )

private val DarkColors =
    darkColorScheme(
        primary = PrimaryDark,
        onPrimary = OnPrimaryDark,
        secondary = SecondaryDark,
        onSecondary = OnSecondaryDark,
    )

@Composable
fun pokeapiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
