package co.edu.uniquindio.ingesis.pokeapi.domain.usecase

import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFilteredPokemonListUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        operator fun invoke(
            query: String,
            type: String?,
            limit: Int = 1000,
            offset: Int = 0,
        ): Flow<List<PokemonListItem>> {
            return repository.observeFilteredPokemonList(query, type, limit, offset)
        }
    }
