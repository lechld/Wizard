package at.aau.edu.wizards.gameModel

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


// FIXME too large tests
// FIXME extract setup parts
class GameModelRulesUnitTest {
    private val viewModel = null

    @Test
    fun test()= runTest {
        val model = GameModel(viewModel)

        val dealer = GameModelDealer(420420)
        val player0 = GameModelPlayer(0, dealer, true, 1, "test")
        val player1 = GameModelPlayer(1, dealer, true, 1, "test")
        val player2 = GameModelPlayer(2, dealer, true, 1, "test")
        val player3 = GameModelPlayer(3, dealer, true, 1, "test")
        val player4 = GameModelPlayer(4, dealer, true, 1, "test")
        val player5 = GameModelPlayer(5, dealer, true, 1, "test")
        val listOfPlayers = ArrayList<GameModelPlayer>()
        listOfPlayers.add(player0)
        listOfPlayers.add(player1)
        listOfPlayers.add(player2)
        listOfPlayers.add(player3)
        listOfPlayers.add(player4)
        listOfPlayers.add(player5)
        val rules = GameModelRules(listOfPlayers, 0, dealer, model, 420420)

        assertEquals(0, rules.round)

        assertThrows<Exception> { rules.playCard(GameModelCard.NoCard) }
        player0.guesses.add(0)
        player1.guesses.add(0)
        player2.guesses.add(0)
        player3.guesses.add(1)
        player4.guesses.add(0)
        player5.guesses.add(1)

        assertEquals(0, rules.id)
        assertEquals(GameModelCard.NoCard, rules.winningCard)

        rules.init()
        assertEquals(1, rules.round)
        rules.init()
        player0.guesses.add(0)
        assertEquals(GameModelCard.NoCard, rules.winningCard)
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Orange, 11), rules.trump)
        assertEquals(0, rules.currentPlayer)
        assertEquals(0, rules.board.size)
        rules.playCard(GameModelCard.Jester(GameModelCard.Color.Orange))
        assertEquals(GameModelCard.Jester(GameModelCard.Color.Orange), rules.winningCard)
        assertEquals(1, rules.currentPlayer)
        assertEquals(0, rules.board.size)
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 6))
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Green, 6), rules.winningCard)
        assertEquals(1, rules.board.size)
        assertFalse(rules.currentPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Orange, 8)))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 4))
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Green, 6), rules.winningCard)
        assertEquals(2, rules.board.size)
        assert(rules.currentPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Orange, 8)))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Orange, 8))
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Orange, 8), rules.winningCard)
        assertEquals(3, rules.board.size)
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 10))
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Orange, 8), rules.winningCard)
        assertEquals(4, rules.board.size)
        assertEquals(5, rules.currentPlayer)
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 5))
        assertEquals(GameModelCard.NoCard, rules.winningCard)
        assertEquals(0, rules.board.size)
        assertEquals(1, rules.currentPlayer)
        assertEquals(20, player0.scores[player0.scores.lastIndex])
        assertEquals(20, player1.scores[player0.scores.lastIndex])
        assertEquals(20, player2.scores[player0.scores.lastIndex])
        assertEquals(30, player3.scores[player0.scores.lastIndex])
        assertEquals(20, player4.scores[player0.scores.lastIndex])
        assertEquals(-10, player5.scores[player0.scores.lastIndex])
        assertEquals(2, rules.round)

        assert(rules.localPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Red, 9)))
        assertFalse(rules.localPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Orange, 10)))
        assert(rules.localPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Blue, 7)))
        assertFalse(rules.localPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Red, 7)))
        assertFalse(rules.localPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Orange, 11)))

        assertFalse(rules.currentPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Red, 9)))
        assert(rules.currentPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Orange, 10)))
        assertFalse(rules.currentPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Blue, 7)))
        assert(rules.currentPlayerOwns(GameModelCard.Normal(GameModelCard.Color.Red, 7)))

        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 7))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 1))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 6))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 3))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 2))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 13))
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Red, 13), rules.winningCard)
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 9))

        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Orange, 10))
        rules.playCard(GameModelCard.Wizard(GameModelCard.Color.Orange))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 3))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 8))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 2))
        assertEquals(GameModelCard.Wizard(GameModelCard.Color.Orange), rules.winningCard)
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 7))

        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 1))
        rules.playCard(GameModelCard.Jester(GameModelCard.Color.Red))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 4))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 3))
        rules.playCard(GameModelCard.Wizard(GameModelCard.Color.Orange))
        rules.playCard(GameModelCard.Wizard(GameModelCard.Color.Blue))

        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 6))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 5))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 7))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 1))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 13))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 7))

        player0.guesses.add(2)
        player2.guesses.add(1)

        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 11))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 9))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 11))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 4))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 12))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 5))

        assertEquals(40, player0.scores[player0.scores.lastIndex])
        assertEquals(30, player2.scores[player2.scores.lastIndex])

        for (i in 1..(4 + 5 + 6 + 7 + 8 + 9) * 6) {
            val cp = rules.currentPlayer
            var iteration = 0
            while (cp == rules.currentPlayer) {
                rules.playCard(listOfPlayers[cp].cards[iteration])
                iteration++
            }
        }

        assertEquals(GameModelCard.NoCard, rules.trump)

        for (i in 1..60) {
            val cp = rules.currentPlayer
            var iteration = 0
            while (cp == rules.currentPlayer) {
                rules.playCard(listOfPlayers[cp].cards[iteration])
                iteration++
            }


        }

    }

    @Test
    fun testWizardTrump() = runTest{
        val model = GameModel(viewModel)

        val dealer = GameModelDealer(420420)
        val player0 = GameModelPlayer(0, dealer, true, 1, "test")
        val player1 = GameModelPlayer(1, dealer, true, 1, "test")
        val player2 = GameModelPlayer(2, dealer, true, 1, "test")
        val player3 = GameModelPlayer(3, dealer, true, 1, "test")
        val player4 = GameModelPlayer(4, dealer, true, 1, "test")
        val player5 = GameModelPlayer(5, dealer, true, 1, "test")
        val listOfPlayers = ArrayList<GameModelPlayer>()
        listOfPlayers.add(player0)
        listOfPlayers.add(player1)
        listOfPlayers.add(player2)
        listOfPlayers.add(player3)
        listOfPlayers.add(player4)
        listOfPlayers.add(player5)
        val rules = GameModelRules(listOfPlayers, 0, dealer, model, 420420)

        player0.guesses.add(1)
        player1.guesses.add(0)
        player2.guesses.add(0)
        player3.guesses.add(1)
        player4.guesses.add(0)
        player5.guesses.add(1)

        dealer.dealCardInSet()
        dealer.dealCardInSet()
        dealer.dealCardInSet()
        dealer.dealCardInSet()
        dealer.dealCardInSet()

        rules.init()

        player0.guesses.add(1)

        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 5))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Orange, 11))
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Green, 5), rules.winningCard)
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 9))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 7))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Orange, 10))
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Green, 5), rules.winningCard)
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 7))

        assertEquals(30, player0.scores[player0.scores.lastIndex])


    }

    @Test
    fun testWizardTrump2() = runTest{
        val model = GameModel(viewModel)
        val dealer = GameModelDealer(22)
        val player0 = GameModelPlayer(0, dealer, true, 1, "test")
        val player1 = GameModelPlayer(1, dealer, true, 1, "test")
        val player2 = GameModelPlayer(2, dealer, true, 1, "test")
        val player3 = GameModelPlayer(3, dealer, true, 1, "test")
        val player4 = GameModelPlayer(4, dealer, true, 1, "test")
        val player5 = GameModelPlayer(5, dealer, true, 1, "test")
        val listOfPlayers = ArrayList<GameModelPlayer>()
        listOfPlayers.add(player0)
        listOfPlayers.add(player1)
        listOfPlayers.add(player2)
        listOfPlayers.add(player3)
        listOfPlayers.add(player4)
        listOfPlayers.add(player5)
        val rules = GameModelRules(listOfPlayers, 0, dealer, model, 420420)

        player0.guesses.add(1)
        player1.guesses.add(0)
        player2.guesses.add(0)
        player3.guesses.add(1)
        player4.guesses.add(0)
        player5.guesses.add(1)

        rules.init()

        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 9))
        rules.playCard(GameModelCard.Jester(GameModelCard.Color.Green))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 8))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 6))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 12))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Green, 10))

        assertEquals(30, player5.scores[player5.scores.lastIndex])
        assertEquals(20, player4.scores[player4.scores.lastIndex])
        assertEquals(-10, player3.scores[player3.scores.lastIndex])

    }

    @Test
    fun testJesterTrump() = runTest{
        val model = GameModel(viewModel)
        val dealer = GameModelDealer(23)
        val player0 = GameModelPlayer(0, dealer, true, 1, "test")
        val player1 = GameModelPlayer(1, dealer, true, 1, "test")
        val player2 = GameModelPlayer(2, dealer, true, 1, "test")
        val player3 = GameModelPlayer(3, dealer, true, 1, "test")
        val player4 = GameModelPlayer(4, dealer, true, 1, "test")
        val player5 = GameModelPlayer(5, dealer, true, 1, "test")
        val listOfPlayers = ArrayList<GameModelPlayer>()
        listOfPlayers.add(player0)
        listOfPlayers.add(player1)
        listOfPlayers.add(player2)
        listOfPlayers.add(player3)
        listOfPlayers.add(player4)
        listOfPlayers.add(player5)
        val rules = GameModelRules(listOfPlayers, 0, dealer, model, 420420)

        player0.guesses.add(1)
        player1.guesses.add(0)
        player2.guesses.add(0)
        player3.guesses.add(1)
        player4.guesses.add(1)
        player5.guesses.add(1)

        rules.init()

        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 7))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 10))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Blue, 9))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Orange, 10))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Red, 11))
        rules.playCard(GameModelCard.Normal(GameModelCard.Color.Orange, 2))

        assertEquals(30, player4.scores[player4.scores.lastIndex])
        assertEquals(20, player2.scores[player2.scores.lastIndex])
        assertEquals(-10, player3.scores[player3.scores.lastIndex])
    }
}