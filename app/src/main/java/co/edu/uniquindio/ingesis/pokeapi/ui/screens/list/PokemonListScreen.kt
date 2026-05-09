package co.edu.uniquindio.ingesis.pokeapi.ui.screens.list

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.edu.uniquindio.ingesis.pokeapi.ui.components.PokemonCard
import co.edu.uniquindio.ingesis.pokeapi.ui.viewmodel.PokemonListViewModel

@Composable
fun PokemonListScreen(
    onPokemonClick: (Int) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= totalItemsCount - 5 && totalItemsCount > 0
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNextPage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            PokemonTopBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                selectedType = uiState.selectedType,
                onTypeSelected = viewModel::onTypeSelected,
                isOnline = uiState.isOnline,
            )
        },
    ) { paddingValues ->
        if (uiState.items.isEmpty() && !uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No se encontraron Pokemons",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            PokemonList(
                items = uiState.items,
                isLoading = uiState.isLoading,
                listState = listState,
                paddingValues = paddingValues,
                onPokemonClick = onPokemonClick,
            )
        }
    }
}

@Composable
private fun PokemonTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedType: String?,
    onTypeSelected: (String) -> Unit,
    isOnline: Boolean,
) {
    val types =
        listOf(
            "grass",
            "fire",
            "water",
            "bug",
            "normal",
            "poison",
            "electric",
            "ground",
            "fairy",
            "fighting",
            "psychic",
            "rock",
            "ghost",
            "ice",
            "dragon",
            "dark",
            "steel",
            "flying",
        )
    Column {
        co.edu.uniquindio.ingesis.pokeapi.ui.components.ConnectionStatusBanner(isOnline = isOnline)
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar Pokemon...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                types.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { onTypeSelected(type) },
                        label = { Text(type) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonList(
    items: List<co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem>,
    isLoading: Boolean,
    listState: androidx.compose.foundation.lazy.LazyListState,
    paddingValues: PaddingValues,
    onPokemonClick: (Int) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding =
            PaddingValues(
                top = paddingValues.calculateTopPadding() + 8.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(items, key = { it.id }) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) },
            )
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
