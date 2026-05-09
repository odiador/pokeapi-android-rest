package co.edu.uniquindio.ingesis.pokeapi.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import co.edu.uniquindio.ingesis.pokeapi.data.local.database.PokemonDatabase
import co.edu.uniquindio.ingesis.pokeapi.data.remote.api.PokemonApiService
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.NamedResourceDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.PokemonDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.PokemonListResponseDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.PokemonSpeciesDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.SpritesDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.TypeDetailDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.TypeSlotDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class PokemonRepositoryImplTest {
    private lateinit var database: PokemonDatabase
    private lateinit var repository: PokemonRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        repository = PokemonRepositoryImpl(FakePokemonApiService(), database.pokemonDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun fetchPokemonPagePersistsListItems() =
        runTest {
            repository.fetchPokemonPage(page = 0, pageSize = 2)

            val items = repository.observePokemonList().first()
            assertEquals(2, items.size)
            assertEquals(1, items[0].id)
            assertEquals("bulbasaur", items[0].name)
            assertEquals(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                items[0].imageUrl,
            )
        }

    @Test
    fun fetchPokemonDetailPersistsDetail() =
        runTest {
            repository.fetchPokemonDetail(1)

            val detail = repository.observePokemonDetail(1).first { it != null }!!
            assertEquals(1, detail.id)
            assertEquals(listOf("grass", "poison"), detail.types)
            assertEquals(7, detail.height)
            assertEquals(69, detail.weight)
        }

    private class FakePokemonApiService : PokemonApiService {
        override suspend fun getPokemonList(
            offset: Int,
            limit: Int,
        ): PokemonListResponseDto {
            return PokemonListResponseDto(
                results =
                    listOf(
                        NamedResourceDto(
                            name = "bulbasaur",
                            url = "https://pokeapi.co/api/v2/pokemon/1/",
                        ),
                        NamedResourceDto(
                            name = "ivysaur",
                            url = "https://pokeapi.co/api/v2/pokemon/2/",
                        ),
                    ),
            )
        }

        override suspend fun getPokemonDetail(id: Int): PokemonDto {
            return when (id) {
                1 ->
                    PokemonDto(
                        id = 1,
                        name = "bulbasaur",
                        height = 7,
                        weight = 69,
                        sprites = SpritesDto(frontDefault = null),
                        types =
                            listOf(
                                TypeSlotDto(NamedResourceDto(name = "grass")),
                                TypeSlotDto(NamedResourceDto(name = "poison")),
                            ),
                    )
                else ->
                    PokemonDto(
                        id = id,
                        name = "pokemon-$id",
                        height = 10,
                        weight = 100,
                        sprites = SpritesDto(frontDefault = null),
                        types = listOf(TypeSlotDto(NamedResourceDto(name = "normal"))),
                    )
            }
        }

        override suspend fun getPokemonByName(name: String): PokemonDto {
            return getPokemonDetail(1)
        }

        override suspend fun getPokemonSpecies(id: Int): PokemonSpeciesDto {
            return PokemonSpeciesDto(
                id = id,
                name = "species-$id",
                flavorTextEntries = emptyList(),
            )
        }

        override suspend fun getTypeDetail(name: String): TypeDetailDto {
            return TypeDetailDto(id = 1, name = name, pokemon = emptyList())
        }

        override suspend fun getTypeNames(): PokemonListResponseDto {
            return PokemonListResponseDto(results = emptyList())
        }
    }
}
