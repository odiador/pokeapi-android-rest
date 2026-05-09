package co.edu.uniquindio.ingesis.pokeapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniquindio.ingesis.pokeapi.data.remote.api.ConnectivityObserver
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.FetchPokemonPageUseCase
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.FetchPokemonsByTypeUseCase
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.GetAvailableTypesUseCase
import co.edu.uniquindio.ingesis.pokeapi.domain.usecase.SearchPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel
    @Inject
    constructor(
        private val observeFilteredPokemonList: co.edu.uniquindio.ingesis.pokeapi.domain.usecase
            .ObserveFilteredPokemonListUseCase,
        private val fetchPokemonPage: FetchPokemonPageUseCase,
        private val searchPokemon: SearchPokemonUseCase,
        private val fetchPokemonsByType: FetchPokemonsByTypeUseCase,
        private val getAvailableTypes: GetAvailableTypesUseCase,
        private val connectivityObserver: ConnectivityObserver,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PokemonListUiState())
        val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

        private var currentPage = 0
        private val currentLimit = MutableStateFlow(100) // Start with 100 items

        init {
            connectivityObserver.observe()
                .onEach { status ->
                    _uiState.update {
                        it.copy(isOnline = status == ConnectivityObserver.Status.Available)
                    }
                }
                .launchIn(viewModelScope)

            // Combinar los filtros y el límite para obtener el listado paginado de la DB
            combine(
                _uiState.map { it.searchQuery }.distinctUntilChanged(),
                _uiState.map { it.selectedType }.distinctUntilChanged(),
                currentLimit,
            ) { query, type, limit ->
                Triple(query, type, limit)
            }.flatMapLatest { (query, type, limit) ->
                observeFilteredPokemonList(query, type, limit)
            }.onEach { items ->
                _uiState.update { it.copy(items = items, isLoading = false) }
            }.launchIn(viewModelScope)

            viewModelScope.launch {
                val types = getAvailableTypes()
                _uiState.update { it.copy(availableTypes = types) }
            }

            loadNextPage()
        }

        fun onSearchQueryChange(query: String) {
            _uiState.update { it.copy(searchQuery = query) }
            val isNumber = query.toIntOrNull() != null
            if (isNumber || query.length >= 3) {
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

        fun refresh() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                currentPage = 0
                runCatching {
                    fetchPokemonPage(0)
                    val types = getAvailableTypes()
                    _uiState.update { it.copy(availableTypes = types) }
                }.onSuccess {
                    currentPage = 1
                    _uiState.update { it.copy(isLoading = false) }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
            }
        }

        fun loadNextPage() {
            if (_uiState.value.isLoading) return

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                // 1. Siempre aumentamos el límite local para ver más de lo que ya tenemos en la DB
                currentLimit.update { it + 50 }

                // 2. Si hay internet y no estamos buscando nada específico, traemos más de la API
                val isSearching =
                    _uiState.value.searchQuery.isNotBlank() || _uiState.value.selectedType != null
                if (_uiState.value.isOnline && !isSearching) {
                    runCatching { fetchPokemonPage(currentPage) }
                        .onSuccess {
                            currentPage += 1
                        }
                        .onFailure { error ->
                            _uiState.update {
                                it.copy(errorMessage = error.message ?: "Error al cargar más datos")
                            }
                        }
                }

                _uiState.update { it.copy(isLoading = false) }
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
