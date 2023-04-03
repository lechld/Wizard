package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class GameModelCardDealerUnitTest {

    @Test
    fun cardDealerTest() {
        val cardDealer = GameModelCardDealer()
        val dealtCards = ArrayList<String>()

        assert(checkGameModelCardWorked(cardDealer.dealCardInSet(1)))
        assert(checkGameModelCardWorked(cardDealer.dealCardInSet(0)))

        cardDealer.resetSet()
        dealtCards.clear()

        for (amount in 1..60) {
            val card = cardDealer.dealCardInSet(0)
            assertFalse(dealtCards.contains(card.getString()))
            dealtCards.add(card.getString())
        }

        assertFalse(checkGameModelCardWorked(cardDealer.dealCardInSet(0)))
    }

    private fun checkGameModelCardWorked(card: GameModelCard): Boolean {
        if (card.color == 0 && card.value == 0 && card.id == 0) {
            return false
        }
        return true
    }
}