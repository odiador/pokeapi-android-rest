package co.edu.uniquindio.ingesis.pokeapi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonDetailEntity

@Dao
interface PokemonDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonDetailEntity)

    @Query("SELECT * FROM pokemon_detail WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): PokemonDetailEntity?

    @Query("SELECT * FROM pokemon_detail WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): PokemonDetailEntity?
}
