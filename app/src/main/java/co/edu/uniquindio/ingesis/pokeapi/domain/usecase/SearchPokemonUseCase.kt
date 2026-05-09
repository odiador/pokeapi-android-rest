package co.edu.uniquindio.ingesis.pokeapi.domain.usecase

import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import javax.inject.Inject

class SearchPokemonUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        suspend operator fun invoke(name: String) = repository.searchPokemon(name)
    }
