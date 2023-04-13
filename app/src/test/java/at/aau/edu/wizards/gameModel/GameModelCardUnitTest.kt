package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GameModelCardUnitTest {

    private val dealer = GameModelDealer(420420)

    @Test
    fun test() {
        for (card in 0..59) {
            assertEquals(card, dealer.getCardFromHash(card).getString()[0].code)
        }

        assertThrows<Exception> {
            GameModelCard.NoCard.getString()
        }
    }
}