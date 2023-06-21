package at.aau.edu.wizards.gameModel

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class GameModelUnitTest {
    private val viewModel = null

    @Test
    fun test() = runTest {
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
        assertFalse(model.sendMessage(buildString {
            append(60.toChar())
            append(60.toChar())
        }))

        assert(model.sendMessage(60.toChar().toString()))

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
    fun sendUpdate() = runTest {
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

    @Test
    fun simulateGame() = runTest {
        val ran = Random(420420)
        val model0 = GameModel(viewModel)
        val model1 = GameModel(viewModel)
        val model2 = GameModel(viewModel)
        model0.receiveMessage(buildString {
            append(0.toChar())
            append(3.toChar())
            append(3.toChar())
            append(420420.toString())
        })
        model1.receiveMessage(buildString {
            append(1.toChar())
            append(3.toChar())
            append(3.toChar())
            append(420420.toString())
        })
        model2.receiveMessage(buildString {
            append(2.toChar())
            append(3.toChar())
            append(3.toChar())
            append(420420.toString())
        })
        assertFalse(model0.sendGuessOfLocalPlayer(-1))
        assertFalse(model0.sendGuessOfLocalPlayer(11))
        for (round in 1..11) {//try to send without guessing
            for (card in model0.listener.getHandOfPlayer(0)) {
                assertFalse(model0.sendMessage(card.getString()))
                assertFalse(model0.receiveMessage(card.getString()))
            }
            var guess = ran.nextInt(0, round)
            assertTrue(model0.sendGuessOfLocalPlayer(guess))
            model0.receiveMessage(buildString {
                append((60 + guess).toChar())
            })
            model1.receiveMessage(buildString {
                append((60 + guess).toChar())
            })
            model2.receiveMessage(buildString {
                append((60 + guess).toChar())
            })
            for (card in model0.listener.getHandOfPlayer(1)) {
                assertFalse(model0.sendMessage(card.getString()))
                //assertFalse(model0.receiveMessage(card.getString()))
            }
            guess = ran.nextInt(0, round)
            model0.receiveMessage(buildString {
                append((71 + guess).toChar())
            })
            model1.receiveMessage(buildString {
                append((71 + guess).toChar())
            })
            model2.receiveMessage(buildString {
                append((71 + guess).toChar())
            })
            for (card in model0.listener.getHandOfPlayer(2)) {
                assertFalse(model0.sendMessage(card.getString()))
                assertFalse(model0.receiveMessage(card.getString()))
            }
            guess = ran.nextInt(0, round)
            model0.receiveMessage(buildString {
                append((82 + guess).toChar())
            })
            model1.receiveMessage(buildString {
                append((82 + guess).toChar())
            })
            model2.receiveMessage(buildString {
                append((82 + guess).toChar())
            })
            for (game in 1..round) {
                for (player in round..round + 2) {
                    for (card in model0.listener.getHandOfPlayer((player - 1) % 3)) {
                        if (model0.sendMessage(card.getString()) || model1.sendMessage(card.getString()) || model2.sendMessage(
                                card.getString()
                            )
                        ) {
                            model0.receiveMessage(card.getString())
                            model1.receiveMessage(card.getString())
                            model2.receiveMessage(card.getString())
                        }
                    }
                }
            }
        }
        assertEquals(11, model0.listener.getRound())
    }
}