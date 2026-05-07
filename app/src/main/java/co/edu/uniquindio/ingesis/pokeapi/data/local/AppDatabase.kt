package co.edu.uniquindio.ingesis.pokeapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import co.edu.uniquindio.ingesis.pokeapi.data.local.dao.PokemonCacheDao
import co.edu.uniquindio.ingesis.pokeapi.data.local.dao.PokemonDetailDao
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonCacheEntity
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonDetailEntity

@Database(
    entities = [PokemonCacheEntity::class, PokemonDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonCacheDao(): PokemonCacheDao
    abstract fun pokemonDetailDao(): PokemonDetailDao
}
