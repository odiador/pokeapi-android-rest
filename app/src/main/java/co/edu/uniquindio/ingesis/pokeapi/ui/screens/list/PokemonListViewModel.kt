package co.edu.uniquindio.ingesis.pokeapi.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import co.edu.uniquindio.ingesis.pokeapi.network.NetworkStatusMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
    val networkMonitor: NetworkStatusMonitor
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private var isLoadingMore = false

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // Load types for filter chips
            try {
                val types = repository.getTypes()
                _uiState.update { it.copy(availableTypes = types) }
            } catch (_: Exception) { }
            // Load first page
            loadNextPageInternal()
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (isLoadingMore || state.hasReachedEnd ||
            state.activeTypeFilter != null || state.searchQuery.isNotEmpty()
        ) return

        viewModelScope.launch {
            loadNextPageInternal()
        }
    }

    private suspend fun loadNextPageInternal() {
        if (isLoadingMore) return
        isLoadingMore = true
        _uiState.update { it.copy(isLoadingMore = true, error = null) }

        try {
            val newPokemon = repository.getPokemonPage(currentPage, PAGE_SIZE)
            if (newPokemon.isEmpty()) {
                _uiState.update { it.copy(hasReachedEnd = true) }
            } else {
                currentPage++
                _uiState.update { state ->
                    state.copy(
                        pokemonList = state.pokemonList + newPokemon,
                        isLoading = false
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message ?: "Error al cargar Pokemon", isLoading = false) }
        } finally {
            isLoadingMore = false
            _uiState.update { it.copy(isLoadingMore = false, isLoading = false) }
        }
    }

    fun searchByName(name: String) {
        if (name.isBlank()) {
            clearFilters()
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, searchQuery = name, activeTypeFilter = null, error = null) }
            try {
                val pokemon = repository.searchPokemon(name.trim().lowercase())
                _uiState.update { state ->
                    state.copy(
                        pokemonList = if (pokemon != null) listOf(pokemon) else emptyList(),
                        isLoading = false,
                        hasReachedEnd = true,
                        error = if (pokemon == null) "No se encontro el Pokemon \"$name\"" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        pokemonList = emptyList(),
                        error = "No se encontro el Pokemon \"$name\"",
                        isLoading = false,
                        hasReachedEnd = true
                    )
                }
            }
        }
    }

    fun filterByType(typeName: String) {
        if (_uiState.value.activeTypeFilter == typeName) {
            clearFilters()
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, activeTypeFilter = typeName, searchQuery = "", error = null) }
            try {
                val pokemon = repository.getPokemonByType(typeName)
                _uiState.update { state ->
                    state.copy(
                        pokemonList = pokemon,
                        isLoading = false,
                        hasReachedEnd = true,
                        error = if (pokemon.isEmpty()) "No hay Pokemon de tipo $typeName guardados sin conexion" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearFilters() {
        currentPage = 0
        isLoadingMore = false
        _uiState.value = PokemonListUiState(availableTypes = _uiState.value.availableTypes)
        viewModelScope.launch {
            loadNextPageInternal()
        }
    }
}
