package at.aau.edu.wizards.model

import at.aau.edu.wizards.api.model.GameModelPlayer
import kotlin.random.Random

data class GameModelCard(
    private val value: Int,
    private val color: Int,
    private val owner: GameModelPlayer
) {
    fun getValue(): Int {
        return value
    }

    fun getColor(): Int {
        return color
    }

    fun checkLegal(): Boolean {
        if (value in 0..14 && color in 1..4) {
            return true
        }
        return false
    }

    fun getOwner(): GameModelPlayer {
        return owner
    }
}

