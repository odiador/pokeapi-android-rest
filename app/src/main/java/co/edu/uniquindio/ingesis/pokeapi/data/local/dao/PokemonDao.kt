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

    @Query(
        """
        SELECT * FROM pokemon_list
        WHERE (:query = '' OR name LIKE '%' || :query || '%' OR CAST(id AS TEXT) = :query)
        AND (:type IS NULL OR typesCsv LIKE '%' || :type || '%')
        ORDER BY id ASC
        LIMIT :limit OFFSET :offset
    """,
    )
    fun observeFilteredPokemonList(
        query: String,
        type: String?,
        limit: Int,
        offset: Int,
    ): Flow<List<PokemonListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(items: List<PokemonListEntity>)

    @Query("SELECT * FROM pokemon_detail WHERE id = :id LIMIT 1")
    fun observePokemonDetail(id: Int): Flow<PokemonDetailEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(entity: PokemonDetailEntity)

    @Query("SELECT name FROM pokemon_types ORDER BY name ASC")
    suspend fun getAllTypes(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypes(types: List<co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonTypeEntity>)
}
