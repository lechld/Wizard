package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GameModelListenerUnitTest {

    @Test
    fun test() {
        val model = GameModel()
        var listener = model.listener

        assertEquals(GameModelCard.NoCard, listener.trump)
        assertEquals(0, listener.numberOfPlayers)
        assertEquals(GameModelCard.NoCard, listener.winningCard)
        assert(listener.getHandOfPlayer(0).isEmpty())

        assert(model.receiveMessage(buildString {
            append(0.toChar())
            append(6.toChar())
            append(0.toChar())
            append(420420.toString())
        }))
        listener = model.listener

        assertEquals(6, listener.numberOfPlayers)
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Orange, 11), listener.trump)


        assertEquals(0, listener.getCurrentGuessOfPlayer(0))
        assertEquals(0, listener.getCurrentGuessOfPlayer(3))

        assert(model.receiveMessage(60.toChar().toString()))
        assert(model.receiveMessage((60 + 11).toChar().toString()))
        assert(model.receiveMessage((60 + 22).toChar().toString()))
        assert(model.receiveMessage((60 + 33 + 1).toChar().toString()))
        assert(model.receiveMessage((60 + 44).toChar().toString()))
        assert(model.receiveMessage((60 + 55).toChar().toString()))


        assertEquals(0, listener.activePlayer)
        assert(listener.board.isEmpty())
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Orange, 11), listener.trump)
        assert(listener.getAllScoresOfPlayer(0).isEmpty())
        assertEquals(0, listener.getCurrentGuessOfPlayer(0))
        assertEquals(0, listener.getCurrentGuessOfPlayer(6))
        assertEquals(1, listener.getCurrentGuessOfPlayer(3))
        assert(listener.getAllScoresOfPlayer(0).isEmpty())
        assert(listener.getHandOfPlayer(6).isEmpty())

        assertEquals(1, listener.getHandOfPlayer(0).size)
        assertEquals(
            GameModelCard.Jester(GameModelCard.Color.Orange),
            listener.getHandOfPlayer(0)[0]
        )

        assert(model.receiveMessage(GameModelCard.Jester(GameModelCard.Color.Orange).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Green, 6).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Blue, 4).getString()))
        assert(
            model.receiveMessage(
                GameModelCard.Normal(GameModelCard.Color.Orange, 8).getString()
            )
        )
        assert(
            model.receiveMessage(
                GameModelCard.Normal(GameModelCard.Color.Green, 10).getString()
            )
        )
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Green, 5).getString()))

        assert(model.receiveMessage(60.toChar().toString()))
        assert(model.receiveMessage((60 + 11).toChar().toString()))
        assert(model.receiveMessage((60 + 22 + 1).toChar().toString()))
        assert(model.receiveMessage((60 + 33 + 1).toChar().toString()))
        assert(model.receiveMessage((60 + 44).toChar().toString()))
        assert(model.receiveMessage((60 + 55).toChar().toString()))

        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Red, 7).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Blue, 1).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Blue, 6).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Red, 3).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Red, 13).getString()))
        assertEquals(GameModelCard.Normal(GameModelCard.Color.Red, 13), listener.winningCard)
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Red, 9).getString()))

        assert(
            model.receiveMessage(
                GameModelCard.Normal(GameModelCard.Color.Orange, 10).getString()
            )
        )
        assert(model.receiveMessage(GameModelCard.Jester(GameModelCard.Color.Orange).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Green, 3).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Red, 8).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Green, 2).getString()))
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Blue, 7).getString()))

        assertEquals(2, listener.getAllScoresOfPlayer(0).size)
        assertEquals(2, listener.getAllScoresOfPlayer(1).size)
        assertEquals(2, listener.getAllScoresOfPlayer(2).size)
        assertEquals(2, listener.getAllScoresOfPlayer(3).size)
        assertEquals(2, listener.getAllScoresOfPlayer(4).size)
        assertEquals(2, listener.getAllScoresOfPlayer(5).size)



        assertEquals(40, listener.getCurrentScoreOfPlayer(0))
        assertEquals(40, listener.getCurrentScoreOfPlayer(1))
        assertEquals(10, listener.getCurrentScoreOfPlayer(2))
        assertEquals(60, listener.getCurrentScoreOfPlayer(3))
        assertEquals(40, listener.getCurrentScoreOfPlayer(4))
        assertEquals(20, listener.getCurrentScoreOfPlayer(5))

        assertEquals(20, listener.getAllScoresOfPlayer(0)[0])
        assertEquals(20, listener.getAllScoresOfPlayer(0)[1])
        assertEquals(20, listener.getAllScoresOfPlayer(1)[0])
        assertEquals(20, listener.getAllScoresOfPlayer(1)[1])
        assertEquals(20, listener.getAllScoresOfPlayer(2)[0])
        assertEquals(-10, listener.getAllScoresOfPlayer(2)[1])
        assertEquals(30, listener.getAllScoresOfPlayer(3)[0])
        assertEquals(30, listener.getAllScoresOfPlayer(3)[1])
        assertEquals(20, listener.getAllScoresOfPlayer(4)[0])
        assertEquals(20, listener.getAllScoresOfPlayer(4)[1])
        assertEquals(20, listener.getAllScoresOfPlayer(5)[0])
        assertEquals(0, listener.getAllScoresOfPlayer(5)[1])

    }
}