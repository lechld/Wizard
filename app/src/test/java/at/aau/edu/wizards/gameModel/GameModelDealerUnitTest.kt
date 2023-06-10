package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class GameModelDealerUnitTest {
    @Test
    fun test() {
        val dealer = GameModelDealer(0)

        val list = ArrayList<GameModelCard>()

        for (i in 1..60) {
            list.add(dealer.dealCardInSet())
        }

        assertEquals(15, getAmountOfColor(list, GameModelCard.Color.Blue))
        assertEquals(15, getAmountOfColor(list, GameModelCard.Color.Red))
        assertEquals(15, getAmountOfColor(list, GameModelCard.Color.Green))
        assertEquals(15, getAmountOfColor(list, GameModelCard.Color.Orange))

        assertEquals(4, getAmountOfType(list, GameModelCard.Jester(GameModelCard.Color.Blue)))
        assertEquals(4, getAmountOfType(list, GameModelCard.Wizard(GameModelCard.Color.Blue)))
        assertEquals(52, getAmountOfType(list, GameModelCard.Normal(GameModelCard.Color.Blue, 1)))
        assertEquals(0, getAmountOfType(list, GameModelCard.NoCard))

        list.add(dealer.dealCardInSet())

        assertEquals(1, getAmountOfType(list, GameModelCard.NoCard))

        dealer.resetSet()

        for (i in 1..60) {
            list.add(dealer.dealCardInSet())
        }

        assertEquals(30, getAmountOfColor(list, GameModelCard.Color.Blue))
        assertEquals(30, getAmountOfColor(list, GameModelCard.Color.Red))
        assertEquals(30, getAmountOfColor(list, GameModelCard.Color.Green))
        assertEquals(30, getAmountOfColor(list, GameModelCard.Color.Orange))

        assertEquals(8, getAmountOfType(list, GameModelCard.Jester(GameModelCard.Color.Blue)))
        assertEquals(8, getAmountOfType(list, GameModelCard.Wizard(GameModelCard.Color.Blue)))
        assertEquals(104, getAmountOfType(list, GameModelCard.Normal(GameModelCard.Color.Blue, 1)))
        assertEquals(1, getAmountOfType(list, GameModelCard.NoCard))

        list.add(dealer.dealCardInSet())

        assertEquals(2, getAmountOfType(list, GameModelCard.NoCard))

        val dealer1 = GameModelDealer(420420)
        val dealer2 = GameModelDealer(420420)

        for (i in 0..100) {
            assert(dealer1.dealCardInSet() == dealer2.dealCardInSet())
            dealer1.resetSet()
            dealer2.resetSet()
        }

    }

    private fun getAmountOfColor(list: ArrayList<GameModelCard>, color: GameModelCard.Color): Int {
        var amount = 0
        for (card in list) {
            when (card) {
                is GameModelCard.Jester -> {
                    if (card.color == color) {
                        amount++
                    }
                }
                GameModelCard.NoCard -> {
                    continue
                }
                is GameModelCard.Normal -> {
                    if (card.color == color) {
                        amount++
                    }
                }
                is GameModelCard.Wizard -> {
                    if (card.color == color) {
                        amount++
                    }
                }
            }
        }
        return amount
    }

    private fun getAmountOfType(list: ArrayList<GameModelCard>, type: GameModelCard): Int {
        var amount = 0
        for (card in list) {
            when (card) {
                is GameModelCard.Jester -> if (type is GameModelCard.Jester) amount++
                GameModelCard.NoCard -> if (type is GameModelCard.NoCard) amount++
                is GameModelCard.Normal -> if (type is GameModelCard.Normal) amount++
                is GameModelCard.Wizard -> if (type is GameModelCard.Wizard) amount++
            }
        }
        return amount
    }
}