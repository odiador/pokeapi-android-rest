package co.edu.uniquindio.ingesis.pokeapi.domain.usecase

import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import javax.inject.Inject

class FetchPokemonPageUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        suspend operator fun invoke(
            page: Int,
            pageSize: Int = PokemonRepository.DEFAULT_PAGE_SIZE,
        ) {
            repository.fetchPokemonPage(page, pageSize)
        }
    }
