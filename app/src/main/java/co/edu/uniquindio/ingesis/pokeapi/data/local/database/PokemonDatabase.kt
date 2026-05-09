package co.edu.uniquindio.ingesis.pokeapi.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.edu.uniquindio.ingesis.pokeapi.data.local.dao.PokemonDao
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonDetailEntity
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonListEntity

@Database(
    entities = [PokemonListEntity::class, PokemonDetailEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
