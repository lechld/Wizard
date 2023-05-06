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

        assertEquals(4, getAmountOfJester(list))
        assertEquals(4, getAmountOfWizard(list))
        assertEquals(52, getAmountOfNormal(list))
        assertEquals(0, getAmountOfNoCard(list))

        list.add(dealer.dealCardInSet())

        assertEquals(1, getAmountOfNoCard(list))

        dealer.resetSet()

        for (i in 1..60) {
            list.add(dealer.dealCardInSet())
        }

        assertEquals(30, getAmountOfColor(list, GameModelCard.Color.Blue))
        assertEquals(30, getAmountOfColor(list, GameModelCard.Color.Red))
        assertEquals(30, getAmountOfColor(list, GameModelCard.Color.Green))
        assertEquals(30, getAmountOfColor(list, GameModelCard.Color.Orange))

        assertEquals(8, getAmountOfJester(list))
        assertEquals(8, getAmountOfWizard(list))
        assertEquals(104, getAmountOfNormal(list))
        assertEquals(1, getAmountOfNoCard(list))

        list.add(dealer.dealCardInSet())

        assertEquals(2, getAmountOfNoCard(list))

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

    private fun getAmountOfNormal(list: ArrayList<GameModelCard>): Int {
        var amount = 0
        for (card in list) {
            when (card) {
                is GameModelCard.Normal -> {
                    amount++
                }
                else -> {
                    continue
                }
            }
        }
        return amount
    }

    private fun getAmountOfJester(list: ArrayList<GameModelCard>): Int {
        var amount = 0
        for (card in list) {
            when (card) {
                is GameModelCard.Jester -> {
                    amount++
                }
                else -> {
                    continue
                }
            }
        }
        return amount
    }

    private fun getAmountOfWizard(list: ArrayList<GameModelCard>): Int {
        var amount = 0
        for (card in list) {
            when (card) {
                is GameModelCard.Wizard -> {
                    amount++
                }
                else -> {
                    continue
                }
            }
        }
        return amount
    }

    private fun getAmountOfNoCard(list: ArrayList<GameModelCard>): Int {
        var amount = 0
        for (card in list) {
            when (card) {
                is GameModelCard.NoCard -> {
                    amount++
                }
                else -> {
                    continue
                }
            }
        }
        return amount
    }
}