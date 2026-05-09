package co.edu.uniquindio.ingesis.pokeapi.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import coil.compose.AsyncImage
import java.util.Locale

@Composable
fun PokemonCard(
    pokemon: PokemonListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryType = pokemon.types.firstOrNull() ?: ""
    val typeColor = co.edu.uniquindio.ingesis.pokeapi.ui.theme.PokemonTypeColors.getColor(primaryType)

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = typeColor.copy(alpha = 0.08f),
            ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier =
                    Modifier
                        .size(80.dp)
                        .background(typeColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.width(16.dp))
            PokemonCardInfo(
                pokemon = pokemon,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun PokemonCardInfo(
    pokemon: PokemonListItem,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "#${String.format(Locale.ROOT, "%03d", pokemon.id)}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = pokemon.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            pokemon.types.forEach { type ->
                androidx.compose.material3.Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = co.edu.uniquindio.ingesis.pokeapi.ui.theme.PokemonTypeColors.getColor(type),
                ) {
                    Text(
                        text = type.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = androidx.compose.ui.graphics.Color.White,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
        }
    }
}
