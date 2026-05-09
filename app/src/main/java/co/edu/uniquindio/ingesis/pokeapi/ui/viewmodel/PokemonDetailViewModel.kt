package co.edu.uniquindio.ingesis.pokeapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.FetchPokemonDetailUseCase
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.ObservePokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel
    @Inject
    constructor(
        private val observePokemonDetail: ObservePokemonDetailUseCase,
        private val fetchPokemonDetail: FetchPokemonDetailUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PokemonDetailUiState())
        val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

        private var currentId: Int? = null

        fun load(id: Int) {
            if (currentId == id) return
            currentId = id

            observePokemonDetail(id)
                .onEach { pokemon ->
                    _uiState.update { it.copy(pokemon = pokemon, isLoading = false) }
                }
                .launchIn(viewModelScope)

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                runCatching { fetchPokemonDetail(id) }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Unknown error",
                            )
                        }
                    }
            }
        }
    }

data class PokemonDetailUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
