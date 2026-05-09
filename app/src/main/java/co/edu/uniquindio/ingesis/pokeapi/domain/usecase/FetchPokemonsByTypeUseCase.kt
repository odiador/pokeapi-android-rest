package co.edu.uniquindio.ingesis.pokeapi.domain.usecase

import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import javax.inject.Inject

class FetchPokemonsByTypeUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(typeName: String) = repository.fetchPokemonsByType(typeName)
}
