package co.edu.uniquindio.ingesis.pokeapi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonCacheEntity

@Dao
interface PokemonCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonCacheEntity>)

    @Query("SELECT * FROM pokemon_cache ORDER BY id ASC")
    suspend fun getAll(): List<PokemonCacheEntity>

    @Query("SELECT * FROM pokemon_cache WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): PokemonCacheEntity?

    @Query("SELECT * FROM pokemon_cache WHERE id IN (:ids) ORDER BY id ASC")
    suspend fun getByIds(ids: List<Int>): List<PokemonCacheEntity>

    @Query("DELETE FROM pokemon_cache")
    suspend fun clearAll()
}
