package co.edu.uniquindio.ingesis.pokeapi.di

import co.edu.uniquindio.ingesis.pokeapi.data.repository.FakePokemonRepository
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePokemonRepository(): PokemonRepository {
        return FakePokemonRepository()
    }
}
