package co.edu.uniquindio.ingesis.pokeapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniquindio.ingesis.pokeapi.data.remote.api.ConnectivityObserver
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.FetchPokemonPageUseCase
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.FetchPokemonsByTypeUseCase
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.ObservePokemonListUseCase
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.SearchPokemonUseCase
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
        private val searchPokemon: SearchPokemonUseCase,
        private val fetchPokemonsByType: FetchPokemonsByTypeUseCase,
        private val connectivityObserver: ConnectivityObserver,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PokemonListUiState())
        val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

        private var currentPage = 0

        init {
            connectivityObserver.observe()
                .onEach { status ->
                    _uiState.update {
                        it.copy(isOnline = status == ConnectivityObserver.Status.Available)
                    }
                }
                .launchIn(viewModelScope)

            observePokemonList()
                .onEach { items ->
                    _uiState.update { state ->
                        val filteredItems =
                            items.filter { item ->
                                val matchesSearch =
                                    state.searchQuery.isBlank() ||
                                        item.name.contains(state.searchQuery, ignoreCase = true)
                                val matchesType =
                                    state.selectedType == null ||
                                        item.types.contains(state.selectedType)
                                matchesSearch && matchesType
                            }
                        state.copy(items = filteredItems, isLoading = false)
                    }
                }
                .launchIn(viewModelScope)

            loadNextPage()
        }

        fun onSearchQueryChange(query: String) {
            _uiState.update { it.copy(searchQuery = query) }
            if (query.length >= 3) {
                performSearch(query)
            }
        }

        private fun performSearch(query: String) {
            viewModelScope.launch {
                searchPokemon(query)
            }
        }

        fun onTypeSelected(type: String) {
            _uiState.update { it.copy(selectedType = type, isLoading = true) }
            viewModelScope.launch {
                runCatching { fetchPokemonsByType(type) }
                    .onFailure { error ->
                        _uiState.update { it.copy(errorMessage = error.message) }
                    }
                _uiState.update { it.copy(isLoading = false) }
            }
        }

        fun loadNextPage() {
            if (_uiState.value.isLoading || _uiState.value.searchQuery.isNotBlank()) return

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
    val searchQuery: String = "",
    val selectedType: String? = null,
    val isOnline: Boolean = true,
)
