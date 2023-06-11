package at.aau.edu.wizards.scoreboard

import at.aau.edu.wizards.R
import at.aau.edu.wizards.gameModel.GameModel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScoreboardViewModelUnitTest {

    private val viewModel = null


    @Test
    fun testScorebaord() = runTest {
        val model = GameModel(viewModel)
        val listener = model.listener

        assertEquals(R.drawable.icon19, listener.getIconFromId(144))
    }

    @Test
    fun testGetIconFromId() = runTest {
        val model = GameModel(viewModel)
        val listener = model.listener

        assertEquals(R.drawable.icon19, listener.getIconFromId(1444))
    }

    @Test
    fun testGetNameOfPlayer() = runTest {
        val model = GameModel(viewModel)
        val listener = model.listener

        assertEquals("MissingPlayer", listener.getNameOfPlayer(-1444))
    }

    @Test
    fun testGetCurrentScoreOfPlayer() = runTest {
        val model = GameModel(viewModel)
        val listener = model.listener

        assertEquals(0, listener.getCurrentScoreOfPlayer(144))
    }
}
