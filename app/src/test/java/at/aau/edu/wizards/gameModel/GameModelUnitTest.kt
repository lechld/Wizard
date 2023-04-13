package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test


class GameModelUnitTest {

    @Test
    fun test() {
        val model = GameModel()
        assertFalse(model.receiveMessage(buildString {
            append(61.toChar())
        }))

        assertFalse(model.receiveMessage(buildString {
            append(15.toChar())
        }))

        assertFalse(model.receiveMessage(buildString {
            append(61.toChar())
            append(15.toChar())
        }))

        assertFalse(model.receiveMessage(buildString {
            append(6.toChar())
            append(0.toChar())
            append(420420.toString())
        }))
        assertFalse(model.receiveMessage(buildString {
            append(6.toChar())
            append(0.toChar())
            append(420420.toString())
        }))

        assertFalse(model.receiveMessage(buildString {
            append(0.toChar())
            append(1.toChar())
            append(1.toChar())
            append(420420.toString())
        }))

        assertFalse(model.receiveMessage(buildString {
            append(4.toChar())
            append(1.toChar())
            append(2.toChar())
            append(420420.toString())
        }))

        assertFalse(model.receiveMessage(buildString {
            append(0.toChar())
            append(3.toChar())
            append(0.toChar())
            append(1.toChar())
        }))

        assert(model.receiveMessage(buildString {
            append(0.toChar())
            append(6.toChar())
            append(0.toChar())
            append(420420.toString())
        }))

        assertFalse(model.sendMessage((60 + 11).toChar().toString()))
        assertFalse(model.sendMessage((60 + 11 + 66).toChar().toString()))
        assertFalse(model.sendMessage(buildString {
            append(60.toChar())
            append(60.toChar())
        }))

        assert(model.sendMessage(60.toChar().toString()))

        assertFalse(model.receiveMessage((60 + 11 + 66).toChar().toString()))
        assert(model.receiveMessage(60.toChar().toString()))
        assert(model.receiveMessage((60 + 11).toChar().toString()))
        assert(model.receiveMessage((60 + 22).toChar().toString()))
        assert(model.receiveMessage((60 + 33 + 1).toChar().toString()))
        assert(model.receiveMessage((60 + 44).toChar().toString()))
        assert(model.receiveMessage((60 + 55).toChar().toString()))

        assertFalse(
            model.sendMessage(
                GameModelCard.Normal(GameModelCard.Color.Green, 6).getString()
            )
        )
        assert(model.sendMessage(GameModelCard.Jester(GameModelCard.Color.Orange).getString()))

        assertFalse(
            model.receiveMessage(
                GameModelCard.Normal(GameModelCard.Color.Green, 6).getString()
            )
        )
        assert(model.receiveMessage(GameModelCard.Jester(GameModelCard.Color.Orange).getString()))
        assertFalse(
            model.sendMessage(
                GameModelCard.Normal(GameModelCard.Color.Green, 6).getString()
            )
        )
        assert(model.receiveMessage(GameModelCard.Normal(GameModelCard.Color.Green, 6).getString()))

        val model2 = GameModel()

        assert(model2.receiveMessage(buildString {
            append(0.toChar())
            append(0.toChar())
            append(6.toChar())
            append(420420.toString())
        }))
    }
}