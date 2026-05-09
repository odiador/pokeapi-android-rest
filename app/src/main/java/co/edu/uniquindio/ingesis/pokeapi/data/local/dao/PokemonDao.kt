package co.edu.uniquindio.ingesis.pokeapi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonDetailEntity
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_list ORDER BY id ASC")
    fun observePokemonList(): Flow<List<PokemonListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(items: List<PokemonListEntity>)

    @Query("SELECT * FROM pokemon_detail WHERE id = :id LIMIT 1")
    fun observePokemonDetail(id: Int): Flow<PokemonDetailEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(entity: PokemonDetailEntity)
}
