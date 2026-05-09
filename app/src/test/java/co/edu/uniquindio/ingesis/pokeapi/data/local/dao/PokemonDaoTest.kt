package co.edu.uniquindio.ingesis.pokeapi.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import co.edu.uniquindio.ingesis.pokeapi.data.local.database.PokemonDatabase
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonDetailEntity
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonListEntity
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
class PokemonDaoTest {
    private lateinit var database: PokemonDatabase
    private lateinit var dao: PokemonDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = database.pokemonDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertPokemonList_persistsAndOrdersById() =
        runTest {
            val items =
                listOf(
                    PokemonListEntity(id = 2, name = "ivysaur", imageUrl = "url-2"),
                    PokemonListEntity(id = 1, name = "bulbasaur", imageUrl = "url-1"),
                )

            dao.insertPokemonList(items)

            val result = dao.observePokemonList().first()
            assertEquals(listOf(1, 2), result.map { it.id })
            assertEquals(listOf("bulbasaur", "ivysaur"), result.map { it.name })
        }

    @Test
    fun insertPokemonDetail_persists() =
        runTest {
            val detail =
                PokemonDetailEntity(
                    id = 25,
                    name = "pikachu",
                    imageUrl = "url-25",
                    typesCsv = "electric",
                    height = 4,
                    weight = 60,
                )

            dao.insertPokemonDetail(detail)

            val stored = dao.observePokemonDetail(25).first()
            assertEquals(detail, stored)
        }
}
