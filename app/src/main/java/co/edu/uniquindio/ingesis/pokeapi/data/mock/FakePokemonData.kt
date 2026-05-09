package co.edu.uniquindio.ingesis.pokeapi.data.mock

import co.edu.uniquindio.ingesis.pokeapi.data.remote.api.ApiConstants
import co.edu.uniquindio.ingesis.pokeapi.domain.model.Pokemon

object FakePokemonData {
    val allPokemon: List<Pokemon> =
        buildList {
            add(pokemon(1, "bulbasaur", listOf("grass", "poison"), 7, 69))
            add(pokemon(2, "ivysaur", listOf("grass", "poison"), 10, 130))
            add(pokemon(3, "venusaur", listOf("grass", "poison"), 20, 1000))
            add(pokemon(4, "charmander", listOf("fire"), 6, 85))
            add(pokemon(5, "charmeleon", listOf("fire"), 11, 190))
            add(pokemon(6, "charizard", listOf("fire", "flying"), 17, 905))
            add(pokemon(7, "squirtle", listOf("water"), 5, 90))
            add(pokemon(8, "wartortle", listOf("water"), 10, 225))
            add(pokemon(9, "blastoise", listOf("water"), 16, 855))
            add(pokemon(10, "caterpie", listOf("bug"), 3, 29))
            add(pokemon(11, "metapod", listOf("bug"), 7, 99))
            add(pokemon(12, "butterfree", listOf("bug", "flying"), 11, 320))
            add(pokemon(13, "weedle", listOf("bug", "poison"), 3, 32))
            add(pokemon(14, "kakuna", listOf("bug", "poison"), 6, 100))
            add(pokemon(15, "beedrill", listOf("bug", "poison"), 10, 295))
            add(pokemon(16, "pidgey", listOf("normal", "flying"), 3, 18))
            add(pokemon(17, "pidgeotto", listOf("normal", "flying"), 11, 300))
            add(pokemon(18, "pidgeot", listOf("normal", "flying"), 15, 395))
            add(pokemon(19, "rattata", listOf("normal"), 3, 35))
            add(pokemon(20, "raticate", listOf("normal"), 7, 185))
            add(pokemon(21, "spearow", listOf("normal", "flying"), 3, 20))
            add(pokemon(22, "fearow", listOf("normal", "flying"), 12, 380))
            add(pokemon(23, "ekans", listOf("poison"), 20, 69))
            add(pokemon(24, "arbok", listOf("poison"), 35, 650))
            add(pokemon(25, "pikachu", listOf("electric"), 4, 60))
            add(pokemon(26, "raichu", listOf("electric"), 8, 300))
            add(pokemon(27, "sandshrew", listOf("ground"), 6, 120))
            add(pokemon(28, "sandslash", listOf("ground"), 10, 295))
            add(pokemon(29, "nidoran-f", listOf("poison"), 4, 70))
            add(pokemon(30, "nidorina", listOf("poison"), 8, 200))
            for (id in 31..60) {
                add(pokemon(id, "pokemon-$id", listOf("normal"), 10, 200))
            }
        }

    private fun pokemon(
        id: Int,
        name: String,
        types: List<String>,
        height: Int,
        weight: Int,
    ): Pokemon {
        return Pokemon(
            id = id,
            name = name,
            imageUrl = spriteUrl(id),
            types = types,
            height = height,
            weight = weight,
        )
    }

    private fun spriteUrl(id: Int): String = "${ApiConstants.SPRITE_BASE_URL}$id.png"
}
