package co.edu.uniquindio.ingesis.pokeapi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_types")
data class PokemonTypeEntity(
    @PrimaryKey val name: String,
)
