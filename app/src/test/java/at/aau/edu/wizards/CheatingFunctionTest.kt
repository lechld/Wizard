package at.aau.edu.wizards

import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.GameModelListener
import at.aau.edu.wizards.gameModel.GameModelPlayer
import at.aau.edu.wizards.gameModel.GameModelRules
import at.aau.edu.wizards.ui.gameboard.GameBoardViewModel
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.mock

class CheatingFunctionTest {
    //TODO: Testing of Cheating function & uncover der Cheating function
    @Mock
    private var rules: GameModelRules = mock()

    @Mock
    private var players: ArrayList<GameModelPlayer> = mock()

    @Mock
    private var viewModel: GameBoardViewModel = mock()

    @Mock
    private var parent: GameModel = mock()

    private var gameModelListener: GameModelListener =
        GameModelListener(rules, players, viewModel, parent)

    @Test
    fun testUpdatedGuess() {
        val playerId = 0
        val initialGuess = 0
        val newGuess = 5

        Assertions.assertTrue(gameModelListener.cheatingFunction)

        gameModelListener.guesses.add(GameModelListener.Guess(initialGuess, playerId))

        gameModelListener.updatedGuess(GameModelListener.Guess(newGuess, playerId))

        val updatedGuess = gameModelListener.getCurrentGuessOfPlayer(playerId)
        assertEquals(newGuess, updatedGuess)

        assertFalse(gameModelListener.cheatingFunction)
    }
}