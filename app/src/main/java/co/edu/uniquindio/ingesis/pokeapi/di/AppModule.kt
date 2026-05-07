package co.edu.uniquindio.ingesis.pokeapi.di

import co.edu.uniquindio.ingesis.pokeapi.data.repository.PokemonRepositoryImpl
import co.edu.uniquindio.ingesis.pokeapi.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository
}
