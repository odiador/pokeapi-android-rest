package co.edu.uniquindio.ingesis.pokeapi.ui.screens.list

import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon

data class PokemonListUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val availableTypes: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasReachedEnd: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val activeTypeFilter: String? = null,
)
