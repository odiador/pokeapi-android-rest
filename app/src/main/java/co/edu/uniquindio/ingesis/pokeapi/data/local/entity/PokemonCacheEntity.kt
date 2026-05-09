package co.edu.uniquindio.ingesis.pokeapi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_cache")
data class PokemonCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val spriteUrl: String?,
)
