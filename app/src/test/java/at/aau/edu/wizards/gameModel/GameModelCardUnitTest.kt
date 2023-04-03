package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class GameModelCardUnitTest {

    private val parent = GameModel()

    @Test
    fun cardTest() {
        val player = GameModelPlayer(0, 0)
        parent.listOfPlayers.add(player)
        var card = GameModelCard(buildString {
            append(1.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        )
        assert(card.value == 1)
        assert(card.color == 1)
        assert(card.id == player.id)
        assertEquals(buildString {
            append(1.toChar())
            append(1.toChar())
            append(0.toChar())
        }, card.getString())
        assert(card.isLegal())
        card = GameModelCard(buildString {
            append(15.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        )
        assertFalse(card.isLegal())
        card = GameModelCard(buildString {
            append(14.toChar())
            append(0.toChar())
            append(0.toChar())
        }
        )
        assertFalse(card.isLegal())
        card = GameModelCard(buildString {
            append(14.toChar())
            append(5.toChar())
            append(0.toChar())
        }
        )
        assertFalse(card.isLegal())
        card = GameModelCard(buildString {
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
        }
        )
        assertFalse(card.isLegal())
    }
}