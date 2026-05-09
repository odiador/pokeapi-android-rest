package co.edu.uniquindio.ingesis.pokeapi.ui.screens.detail

import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonDetail

data class PokemonDetailUiState(
    val detail: PokemonDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
