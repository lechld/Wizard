package at.aau.edu.wizards.gameModel

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class GameModelPopUpListenerUnitTest {
    private val viewModel = null

    @Test
    fun test() = runTest {
        val model = GameModel(viewModel)
        var listener = model.listener

        assertEquals(GameModelCard.NoCard, listener.winningCardPopUp.lastCardWon)

        assertEquals(false, listener.winningCardPopUp.visible)
        assertEquals(false, listener.winningCardPopUp.winner)

        assertEquals(0, listener.getPointDifference())
        assertEquals(0, listener.getWinnerRoundInt())
        assertEquals("No Winner", listener.getWinnerRound())
    }

    @Test
    fun testPointDifference() = runTest {
        val model = GameModel(viewModel)
        var listener = model.listener

        assertEquals(0, listener.getPointDifference())

        val player0 = GameModelListener.ScoreRound(10, 0)
        val player1 = GameModelListener.ScoreRound(20, 1)
        val player2 = GameModelListener.ScoreRound(-10, 2)
        val player3 = GameModelListener.ScoreRound(40, 3)
        val player4 = GameModelListener.ScoreRound(80, 4)

        val playerScoreList = ArrayList<GameModelListener.ScoreRound>()
        playerScoreList.add(player0)
        playerScoreList.add(player1)
        playerScoreList.add(player2)
        playerScoreList.add(player3)
        playerScoreList.add(player4)

        //TODO HILFE
        val mock = mock<GameModelListener>()

        mock.scoresRound = playerScoreList

        assertEquals(40, listener.getPointDifference())
    }
}