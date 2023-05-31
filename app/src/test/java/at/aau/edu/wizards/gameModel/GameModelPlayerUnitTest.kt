package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GameModelPlayerUnitTest {

    // FIXME too large test
    @Test
    fun test() {
        val dealer1 = GameModelDealer(420420)
        val dealer2 = GameModelDealer(420420)
        val player = GameModelPlayer(0, dealer1, true, 1, "test")

        assertEquals(true, player.isHuman)
        assertEquals(0, player.id)

        for (i in 0..10) {
            player.dealCards(i)
            for (k in 1..i) {
                assert(player.cards.contains(dealer2.dealCardInSet()))
            }
            player.cards.clear()
            dealer1.resetSet()
            dealer2.resetSet()
        }

        for (i in 1..60) {
            player.dealCards(1)
            when (val card = dealer2.dealCardInSet()) {
                is GameModelCard.Normal -> {
                    when (card.color) {
                        GameModelCard.Color.Blue -> {
                            assert(player.hasColor(GameModelCard.Color.Blue))
                            assertFalse(player.hasColor(GameModelCard.Color.Red))
                            assertFalse(player.hasColor(GameModelCard.Color.Orange))
                            assertFalse(player.hasColor(GameModelCard.Color.Green))
                        }
                        GameModelCard.Color.Green -> {
                            assertFalse(player.hasColor(GameModelCard.Color.Blue))
                            assertFalse(player.hasColor(GameModelCard.Color.Red))
                            assertFalse(player.hasColor(GameModelCard.Color.Orange))
                            assert(player.hasColor(GameModelCard.Color.Green))
                        }
                        GameModelCard.Color.Orange -> {
                            assertFalse(player.hasColor(GameModelCard.Color.Blue))
                            assertFalse(player.hasColor(GameModelCard.Color.Red))
                            assert(player.hasColor(GameModelCard.Color.Orange))
                            assertFalse(player.hasColor(GameModelCard.Color.Green))
                        }
                        GameModelCard.Color.Red -> {
                            assertFalse(player.hasColor(GameModelCard.Color.Blue))
                            assert(player.hasColor(GameModelCard.Color.Red))
                            assertFalse(player.hasColor(GameModelCard.Color.Orange))
                            assertFalse(player.hasColor(GameModelCard.Color.Green))
                        }
                    }
                }
                else -> {
                    continue
                }
            }
            player.cards.clear()
            dealer1.resetSet()
            dealer2.resetSet()
        }

        assertThrows<Exception> { player.score(0) }
        assertThrows<Exception> { player.score(3) }

        player.guesses.add(0)

        player.score(0)
        player.score(2)

        assertEquals(20, player.scores[0])
        assertEquals(0, player.scores[1])

        player.guesses.add(2)

        player.score(0)
        player.score(2)

        assertEquals(-20, player.scores[2])
        assertEquals(40, player.scores[3])

        player.guesses.add(10)

        player.score(0)
        player.score(11)

        assertEquals(-100, player.scores[4])
        assertEquals(100, player.scores[5])
    }


}