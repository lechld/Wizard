package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GameModelDataListenerUnitTest {
    @Test
    fun dataListenerTest() {
        val model = GameModel()
        val rules = GameModelRules(model)
        val listener = GameModelDataListener(model, rules)
        val listener2 = model.getListener()

        model.listOfPlayers.add(GameModelPlayer(0, 0))
        model.listOfPlayers.add(GameModelPlayer(1, 1))

        model.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        model.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        rules.addTrump(
            buildString {
                append(6.toChar())
                append(1.toChar())
                append(6.toChar())
            }
        )
        rules.initFirstRound()

        assertEquals(0, listener.getActivePlayer())
        assertEquals(buildString {
            append(0.toChar())
            append(0.toChar())
            append(0.toChar())
        }, listener.getActiveTrump().getString())
        assertEquals(0, listener.getCurrentGuessOfPlayer(0))
        assertEquals(0, listener.getCurrentGuessOfPlayer(100))
        assertEquals(0, listener.getTotalNumberOfPlayers())
        assert(listener.getCurrentHandOfPlayer(0).isEmpty())
        assert(listener.getAllScoresOfPlayer(0).isEmpty())
        assertEquals(0, listener.getCurrentScoreOfPlayer(0))
        assertEquals(0, listener.getCurrentScoreOfPlayer(100))

        listener.update()
        listener2.update()

        assertEquals(0, listener.getActivePlayer())
        assertEquals(buildString {
            append(6.toChar())
            append(1.toChar())
            append(6.toChar())
        }, listener.getActiveTrump().getString())
        assertEquals(1, listener.getCurrentGuessOfPlayer(0))
        assertEquals(0, listener.getCurrentGuessOfPlayer(100))
        assertEquals(2, listener.getTotalNumberOfPlayers())
        assert(listener.getAllScoresOfPlayer(0).isEmpty())
        assertEquals(0, listener.getCurrentScoreOfPlayer(0))
        assertEquals(0, listener.getCurrentScoreOfPlayer(100))

        val card = listener.getCurrentHandOfPlayer(0)[0].getString()

        assertEquals(
            buildString {
                append(4.toChar())
                append(1.toChar())
                append(0.toChar())
            },
            card
        )

        model.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        model.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }))
        model.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(2.toChar())
            append(0.toChar())
        }
        ))
        model.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(2.toChar())
            append(1.toChar())
        }))
        rules.addTrump(
            buildString {
                append(6.toChar())
                append(1.toChar())
                append(6.toChar())
            }
        )


        model.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        listener.update()
        listener2.update()
        model.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }))
        listener.update()
        listener2.update()
        model.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        listener.update()
        listener2.update()
        model.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }))
        listener.update()
        listener2.update()
        model.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        listener.update()
        listener2.update()
        model.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }))
        listener.update()
        listener2.update()
        rules.addTrump(
            buildString {
                append(6.toChar())
                append(1.toChar())
                append(6.toChar())
            }
        )
        listener.update()
        listener2.update()

        rules.id = 0
        //model.receiveMove(buildString {
        //    append(4.toChar())
        //    append(1.toChar())
        //    append(0.toChar())
        //})
        rules.playCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        })
        rules.playCard(buildString {
            append(5.toChar())
            append(1.toChar()) //30p
            append(1.toChar())
        })
        listener.update()
        listener2.update()
        rules.playCard(buildString {
            append(5.toChar())
            append(2.toChar()) //p0 wins
            append(1.toChar())
        })
        listener.update()
        listener2.update()
        rules.playCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        })
        listener.update()
        listener2.update()
        rules.playCard(buildString {
            append(5.toChar())
            append(1.toChar()) //p1 wins
            append(1.toChar())
        })

        assertEquals(0, listener.getCurrentGuessOfPlayer(1))
        listener.update()
        listener2.update()
        rules.playCard(buildString {
            append(4.toChar())
            append(2.toChar())
            append(0.toChar())
        })
        listener.update()
        listener2.update()

        assertEquals(20, listener.getCurrentScoreOfPlayer(0))
        assertEquals(0, listener.getCurrentScoreOfPlayer(1))

        var scoreList = listener.getAllScoresOfPlayer(0)

        assertEquals(2, scoreList.size)

        assert(scoreList[0] == -10)
        assert(scoreList[1] == 30)

        scoreList = listener.getAllScoresOfPlayer(1)
        assert(scoreList[0] == 0)
        assert(scoreList[1] == 0)

        assertEquals(listener2.getAllScoresOfPlayer(0), listener.getAllScoresOfPlayer(0))
        assertEquals(listener2.getAllScoresOfPlayer(1), listener.getAllScoresOfPlayer(1))
        assertEquals(
            listener2.getCurrentHandOfPlayer(0)[2].getString(),
            listener.getCurrentHandOfPlayer(0)[2].getString()
        )
        assertEquals(
            listener2.getCurrentHandOfPlayer(1)[2].getString(),
            listener.getCurrentHandOfPlayer(1)[2].getString()
        )
    }
}