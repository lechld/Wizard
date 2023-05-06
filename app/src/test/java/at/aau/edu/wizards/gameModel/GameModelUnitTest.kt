package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random


class GameModelUnitTest {
    private val viewModel = null
    @Test
    fun test() {
        val model = GameModel(viewModel)
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

        assertEquals(0, model.localPlayer())

        val model2 = GameModel(viewModel)

        assert(model2.receiveMessage(buildString {
            append(0.toChar())
            append(0.toChar())
            append(6.toChar())
            append(420420.toString())
        }))
    }

    @Test
    fun sendUpdate() {
        val ran = Random(420420)
        val model = GameModel(viewModel)
        model.receiveMessage(buildString {
            append(0.toChar())
            append(1.toChar())
            append(5.toChar())
            append(420420.toString())
        })
        for (k in 1..10) {

            val guess = ran.nextInt(0, k)
            model.receiveMessage(buildString {
                append((60 + guess).toChar())
            })
            for (j in 1..k) {
                var sent = false
                for (i in 1..5) {
                    for (card in model.listener.getHandOfPlayer(i)) {
                        assertFalse(model.sendMessage(card.getString()))
                    }
                }
                for (card in model.listener.getHandOfPlayer(0)) {
                    if (!sent && model.sendMessage(card.getString())) {
                        model.receiveMessage(card.getString())
                        sent = true
                    }
                }
                if (!sent) {
                    continue
                }
            }
        }
    }
}