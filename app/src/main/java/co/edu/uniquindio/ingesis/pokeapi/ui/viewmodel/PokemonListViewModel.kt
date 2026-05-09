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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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

            // Combinar el Flow de la DB con los cambios de búsqueda/filtro de la UI
            combine(
                observePokemonList(),
                _uiState.map { it.searchQuery }.distinctUntilChanged(),
                _uiState.map { it.selectedType }.distinctUntilChanged(),
            ) { items, query, type ->
                items.filter { item ->
                    val matchesSearch =
                        query.isBlank() ||
                            item.name.contains(query, ignoreCase = true)
                    val matchesType =
                        type == null ||
                            item.types.contains(type)
                    matchesSearch && matchesType
                }
            }.onEach { filteredItems ->
                _uiState.update { it.copy(items = filteredItems, isLoading = false) }
            }.launchIn(viewModelScope)

            viewModelScope.launch {
                val types = getAvailableTypes()
                _uiState.update { it.copy(availableTypes = types) }
            }

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
            val actualType = type.takeIf { it.isNotBlank() }
            _uiState.update { it.copy(selectedType = actualType, isLoading = actualType != null) }

            if (actualType != null) {
                viewModelScope.launch {
                    runCatching { fetchPokemonsByType(actualType) }
                        .onFailure { error ->
                            _uiState.update { it.copy(errorMessage = error.message) }
                        }
                    _uiState.update { it.copy(isLoading = false) }
                }
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
    val availableTypes: List<String> = emptyList(),
)
