package co.edu.uniquindio.ingesis.pokeapi.domain.usecase

import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePokemonDetailUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        operator fun invoke(id: Int): Flow<Pokemon?> = repository.observePokemonDetail(id)
    }
