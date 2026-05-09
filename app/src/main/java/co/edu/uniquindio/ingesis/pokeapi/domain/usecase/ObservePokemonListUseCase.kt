package co.edu.uniquindio.ingesis.pokeapi.domain.usecase

import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePokemonListUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        operator fun invoke(): Flow<List<PokemonListItem>> = repository.observePokemonList()
    }
