package co.edu.uniquindio.ingesis.pokeapi.data.mapper

import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonDetailEntity
import co.edu.uniquindio.ingesis.pokeapi.data.local.entity.PokemonListEntity
import co.edu.uniquindio.ingesis.pokeapi.data.remote.api.ApiConstants
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.NamedResourceDto
import co.edu.uniquindio.ingesis.pokeapi.data.remote.dto.PokemonDto
import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon
import co.edu.uniquindio.ingesis.pokeapi.domain.model.PokemonListItem

private fun spriteUrlFor(id: Int): String = "${ApiConstants.SPRITE_BASE_URL}$id.png"

fun NamedResourceDto.toListItemOrNull(): PokemonListItem? {
    val id =
        url
            ?.trimEnd('/')
            ?.substringAfterLast('/')
            ?.toIntOrNull()
    return id?.let { parsedId ->
        PokemonListItem(
            id = parsedId,
            name = name,
            imageUrl = spriteUrlFor(parsedId),
        )
    }
}

fun PokemonListItem.toEntity(): PokemonListEntity =
    PokemonListEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )

fun PokemonListEntity.toDomain(): PokemonListItem =
    PokemonListItem(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )

fun PokemonDto.toDomain(): Pokemon =
    Pokemon(
        id = id,
        name = name,
        imageUrl = sprites.frontDefault ?: spriteUrlFor(id),
        types = types.map { it.type.name },
        height = height,
        weight = weight,
    )

fun PokemonDto.toDetailEntity(): PokemonDetailEntity =
    PokemonDetailEntity(
        id = id,
        name = name,
        imageUrl = sprites.frontDefault ?: spriteUrlFor(id),
        typesCsv = types.joinToString(",") { it.type.name },
        height = height,
        weight = weight,
    )

fun PokemonDetailEntity.toDomain(): Pokemon =
    Pokemon(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = typesCsv.split(',').filter { it.isNotBlank() },
        height = height,
        weight = weight,
    )
