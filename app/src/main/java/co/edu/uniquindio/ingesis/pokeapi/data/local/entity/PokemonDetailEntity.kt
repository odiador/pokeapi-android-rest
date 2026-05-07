package co.edu.uniquindio.ingesis.pokeapi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_detail")
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int?,
    val spriteUrl: String?,
    val typesJson: String,       // JSON array: ["grass","poison"]
    val abilitiesJson: String,   // JSON array: [{"name":"overgrow","isHidden":false}]
    val statsJson: String        // JSON array: [{"name":"hp","value":45}]
)
