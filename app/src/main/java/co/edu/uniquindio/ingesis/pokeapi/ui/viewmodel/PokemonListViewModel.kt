package co.edu.uniquindio.ingesis.pokeapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.FetchPokemonPageUseCase
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.ObservePokemonListUseCase
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
class PokemonListViewModel
    @Inject
    constructor(
        private val observePokemonList: ObservePokemonListUseCase,
        private val fetchPokemonPage: FetchPokemonPageUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PokemonListUiState())
        val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

        private var currentPage = 0

        init {
            observePokemonList()
                .onEach { items ->
                    _uiState.update { it.copy(items = items, isLoading = false) }
                }
                .launchIn(viewModelScope)

            loadNextPage()
        }

        fun loadNextPage() {
            if (_uiState.value.isLoading) return

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                runCatching { fetchPokemonPage(currentPage) }
                    .onSuccess {
                        currentPage += 1
                        _uiState.update { it.copy(isLoading = false) }
                    }
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

data class PokemonListUiState(
    val items: List<PokemonListItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
