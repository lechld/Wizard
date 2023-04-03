package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class GameModelPlayerUnitTest {

    private val test = GameModelUnitTest()

    @Test
    fun playerCardTest() {
        val parent = GameModel()
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))
        val player = parent.listOfPlayers[0]
        val cpu = parent.listOfPlayers[1]

        assertEquals(0, player.isCPU)
        assertEquals(1, cpu.isCPU)

        assertEquals(
            buildString {
                append(15.toChar())
                append(0.toChar())
                append(0.toChar())
            }, player.getString()
        )

        assertEquals(
            buildString {
                append(15.toChar())
                append(1.toChar())
                append(1.toChar())
            }, cpu.getString()
        )

        assertFalse(test.resultUnit(player.dealHand(1)))
        assertFalse(test.resultUnit(player.dealHand(0)))

        val card1 = GameModelCard(
            buildString {
                append(4.toChar())
                append(1.toChar())
                append(0.toChar())
            }
        )

        val card2 = GameModelCard(
            buildString {
                append(0.toChar())
                append(3.toChar())
                append(0.toChar())
            }
        )

        val card3 = GameModelCard(
            buildString {
                append(14.toChar())
                append(2.toChar())
                append(0.toChar())
            }
        )

        assertFalse(test.resultUnit(player.removeCardFromHand(card1)))
        assertFalse(player.cardsContain(card1.toString()))
        assertFalse(player.cardsContainColor(1))
        assertFalse(player.cardsContainColor(2))
        assertFalse(player.cardsContainColor(3))
        assertFalse(player.cardsContainColor(4))

        assertFalse(
            test.resultUnit(
                player.addCardToPlayerStack(
                    GameModelCard(
                        buildString {
                            append(15.toChar())
                            append(2.toChar())
                            append(0.toChar())
                        }
                    )
                )
            )
        )
        assertFalse(
            test.resultUnit(
                player.addCardToPlayerStack(
                    GameModelCard(
                        buildString {
                            append(14.toChar())
                            append(2.toChar())
                            append(1.toChar())
                        }
                    )
                )
            )
        )
        assertFalse(
            test.resultUnit(
                player.addCardToPlayerStack(
                    GameModelCard(
                        buildString {
                            append(5.toChar())
                            append(5.toChar())
                            append(1.toChar())
                        }
                    )
                )
            )
        )

        assertFalse(test.resultUnit(player.removeCardFromHand(card1)))
        assertFalse(player.cardsContain(card1.toString()))
        assertFalse(player.cardsContainColor(1))
        assertFalse(player.cardsContainColor(2))
        assertFalse(player.cardsContainColor(3))
        assertFalse(player.cardsContainColor(4))

        assert(test.resultUnit(player.addCardToPlayerStack(card1)))
        assertEquals(
            buildString {
                append(15.toChar())
                append(0.toChar())
                append(0.toChar())
                append(4.toChar())
                append(1.toChar())
                append(0.toChar())
            }, player.getString()
        )
        assert(test.resultUnit(player.dealHand(1)))

        assert(player.cardsContain(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }))
        assert(player.cardsContainColor(1))

        assert(test.resultUnit(player.addCardToPlayerStack(card2)))
        assert(test.resultUnit(player.addCardToPlayerStack(card3)))

        assertFalse(test.resultUnit(player.removeCardFromHand(card3)))
        assertFalse(player.cardsContain(card2.toString()))

        assertFalse(test.resultUnit(player.dealHand(2)))
        assert(test.resultUnit(player.removeCardFromHand(card1)))
        assert(player.cardsEmpty())
        assert(test.resultUnit(player.dealHand(2)))
        assertFalse(player.cardsEmpty())
        assertFalse(test.resultUnit(player.dealHand(3)))

        assertFalse(player.cardsContainColor(1))
        assertFalse(player.cardsContainColor(2))
        assertFalse(player.cardsContainColor(3))
        assertFalse(player.cardsContainColor(4))

        assert(test.resultUnit(player.removeCardFromHand(card2)))
        assert(test.resultUnit(player.removeCardFromHand(card3)))

        val listOfCards = ArrayList<GameModelCard>()
        val dealer = GameModelCardDealer()

        for (round in 3..10) {
            for (game in 1..round) {
                val card = dealer.dealCardInSet(0)
                listOfCards.add(card)
                assert(test.resultUnit(player.addCardToPlayerStack(card)))
            }
            dealer.resetSet()
            assert(test.resultUnit(player.dealHand(round)))
            for (card in listOfCards) {
                assert(test.resultUnit(player.removeCardFromHand(card)))
            }
            listOfCards.clear()
        }
        for (amount in 1..11) {
            val card = dealer.dealCardInSet(0)
            assert(test.resultUnit(player.addCardToPlayerStack(card)))

        }
        assertFalse(test.resultUnit(player.dealHand(11)))
    }
}