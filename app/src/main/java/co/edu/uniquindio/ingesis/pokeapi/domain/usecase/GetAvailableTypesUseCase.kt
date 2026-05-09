package co.edu.uniquindio.ingesis.pokeapi.domain.usecase

import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import javax.inject.Inject

class GetAvailableTypesUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        suspend operator fun invoke(): List<String> {
            return repository.getAvailableTypes()
        }
    }
