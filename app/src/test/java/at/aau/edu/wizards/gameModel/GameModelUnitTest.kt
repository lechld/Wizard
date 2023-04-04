package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class GameModelUnitTest {

    fun resultUnit(result: GameModelResult<Unit>): Boolean {
        return when (result) {
            is GameModelResult.Failure -> {
                false
            }
            is GameModelResult.Success -> {
                true
            }
        }
    }

    fun resultInt(result: GameModelResult<Int>): Boolean {
        return when (result) {
            is GameModelResult.Failure -> {
                false
            }
            is GameModelResult.Success -> {
                true
            }
        }
    }

    fun resultGameModelCard(result: GameModelResult<GameModelCard>): Boolean {
        return when (result) {
            is GameModelResult.Failure -> {
                false
            }
            is GameModelResult.Success -> {
                true
            }
        }
    }

    fun resultString(result: GameModelResult<String>): Boolean {
        return when (result) {
            is GameModelResult.Failure -> {
                false
            }
            is GameModelResult.Success -> {
                true
            }
        }
    }

    @Test
    fun gameModelMessageTest() {
        val model = GameModel()
        model.listOfPlayers.add(GameModelPlayer(0, 0))
        model.listOfPlayers.add(GameModelPlayer(1, 1))

        assertFalse(resultUnit(model.sendMove(buildString {
            append(1.toChar())
        })))

        assertFalse(resultUnit(model.sendMove(buildString {
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model.listOfPlayers[0].addCardToPlayerStack(
            GameModelCard(
                buildString {
                    append(1.toChar())
                    append(1.toChar())
                    append(0.toChar())
                }
            )
        )

        model.listOfPlayers[1].addCardToPlayerStack(
            GameModelCard(
                buildString {
                    append(2.toChar())
                    append(1.toChar())
                    append(1.toChar())
                }
            )
        )

        assertFalse(resultUnit(model.sendMove(buildString {
            append(1.toChar())
            append(1.toChar())
            append(0.toChar())
        })))

        assertFalse(resultUnit(model.sendMove(
            buildString {
                append(9.toChar())
            }
        )))
        assert(resultUnit(model.sendMove(
            buildString {
                append(10.toChar())
            }
        )))
        model.mockNetworkDoneSending()
        assert(resultUnit(model.sendMove(
            buildString {
                append(31.toChar())
            }
        )))
        model.mockNetworkDoneSending()
        assertFalse(resultUnit(model.sendMove(
            buildString {
                append(32.toChar())
            }
        )))

        assertEquals(0, model.getListener().getCurrentGuessOfPlayer(0))
        assertEquals(10, model.getListener().getCurrentGuessOfPlayer(1))
    }

    @Test
    fun testSendWithConfig() {
        val model = GameModel()
        val config = GameModelConfig(model)
        val model2 = GameModel()
        val model3 = GameModel()

        config.createConfig(3, 0)
        when (val conf = config.getConfig()) {
            is GameModelResult.Failure -> {
                assert(false) //should not happen
            }
            is GameModelResult.Success -> {
                assert(resultUnit(model2.receiveMove(conf.output)))
            }
        }
        when (val conf = config.getConfig()) {
            is GameModelResult.Failure -> {
                assert(false) //should not happen
            }
            is GameModelResult.Success -> {
                assert(resultUnit(model3.receiveMove(conf.output)))
            }
        }

        var move = getRandomPlayerCard(model.listOfPlayers[0])
        assert(resultUnit(model.sendMove(move)))
        assert(resultUnit(model2.receiveMove(move)))
        assert(resultUnit(model3.receiveMove(move)))
        model.mockNetworkDoneSending()
        move = getRandomPlayerCard(model.listOfPlayers[1])
        assertFalse(resultUnit(model.sendMove(move)))
        assert(resultUnit(model2.sendMove(move)))
        assert(resultUnit(model3.receiveMove(move)))
        assert(resultUnit(model.receiveMove(move)))
        model2.mockNetworkDoneSending()
        move = getRandomPlayerCard(model.listOfPlayers[2])
        assert(resultUnit(model.receiveMove(move)))
        assert(resultUnit(model2.receiveMove(move)))
        assert(resultUnit(model3.sendMove(move)))
        model3.mockNetworkDoneSending()

        move = getRandomPlayerCard(model.listOfPlayers[0])
        var moveError = getRandomPlayerCard(model.listOfPlayers[0])
        while (moveError == move) {
            moveError = getRandomPlayerCard(model.listOfPlayers[0])
        }
        assertFalse(resultUnit(model.sendMove(move)))
        assertFalse(resultUnit(model.sendMove(moveError)))
        assertFalse(resultUnit(model2.receiveMove(move)))
        assertFalse(resultUnit(model3.receiveMove(move)))
        move = getRandomPlayerCard(model.listOfPlayers[1])
        assertFalse(resultUnit(model.sendMove(move)))
        assert(resultUnit(model2.sendMove(move)))
        assert(resultUnit(model3.receiveMove(move)))
        assert(resultUnit(model.receiveMove(move)))
        model2.mockNetworkDoneSending()
        move = getRandomPlayerCard(model.listOfPlayers[2])
        assert(resultUnit(model.receiveMove(move)))
        assert(resultUnit(model2.receiveMove(move)))
        assert(resultUnit(model3.sendMove(move)))
        model3.mockNetworkDoneSending()

        move = getRandomPlayerCard(model.listOfPlayers[0])
        while (moveError == move) {
            moveError = getRandomPlayerCard(model.listOfPlayers[0])
        }

        assert(resultUnit(model.sendMove(move)))
        assertFalse(resultUnit(model.sendMove(moveError)))
        model.mockNetworkDoneSending()
        assert(resultUnit(model2.receiveMove(move)))
        assert(resultUnit(model3.receiveMove(move)))

        move = getRandomPlayerCard(model.listOfPlayers[1])
        assertFalse(resultUnit(model.sendMove(move)))
        assert(resultUnit(model2.sendMove(move)))
        assert(resultUnit(model3.receiveMove(move)))
        assert(resultUnit(model.receiveMove(move)))
        model2.mockNetworkDoneSending()
        move = getRandomPlayerCard(model.listOfPlayers[2])
        assert(resultUnit(model.receiveMove(move)))
        assert(resultUnit(model2.receiveMove(move)))
        assert(resultUnit(model3.sendMove(move)))
        model3.mockNetworkDoneSending()
        move = getRandomPlayerCard(model.listOfPlayers[0])
        assert(resultUnit(model.sendMove(move)))
        assert(resultUnit(model2.receiveMove(move)))
        assert(resultUnit(model3.receiveMove(move)))
        model.mockNetworkDoneSending()

        for (turn in 3..10) {
            for (game in 1..turn) {
                if (turn % 3 == 0) {
                    move = getRandomPlayerCard(model.listOfPlayers[2])
                    assert(resultUnit(model.receiveMove(move)))
                    assert(resultUnit(model2.receiveMove(move)))
                    assert(resultUnit(model3.sendMove(move)))
                    model3.mockNetworkDoneSending()
                    move = getRandomPlayerCard(model.listOfPlayers[0])
                    assert(resultUnit(model.sendMove(move)))
                    assert(resultUnit(model2.receiveMove(move)))
                    assert(resultUnit(model3.receiveMove(move)))
                    model.mockNetworkDoneSending()
                    move = getRandomPlayerCard(model.listOfPlayers[1])
                    assert(resultUnit(model2.sendMove(move)))
                    assert(resultUnit(model3.receiveMove(move)))
                    assert(resultUnit(model.receiveMove(move)))
                    model2.mockNetworkDoneSending()
                } else if (turn % 3 == 1) {
                    move = getRandomPlayerCard(model.listOfPlayers[0])
                    assert(resultUnit(model.sendMove(move)))
                    assert(resultUnit(model2.receiveMove(move)))
                    assert(resultUnit(model3.receiveMove(move)))
                    model.mockNetworkDoneSending()
                    move = getRandomPlayerCard(model.listOfPlayers[1])
                    assert(resultUnit(model2.sendMove(move)))
                    assert(resultUnit(model3.receiveMove(move)))
                    assert(resultUnit(model.receiveMove(move)))
                    model2.mockNetworkDoneSending()
                    move = getRandomPlayerCard(model.listOfPlayers[2])
                    assert(resultUnit(model.receiveMove(move)))
                    assert(resultUnit(model2.receiveMove(move)))
                    assert(resultUnit(model3.sendMove(move)))
                    model3.mockNetworkDoneSending()
                } else {
                    move = getRandomPlayerCard(model.listOfPlayers[1])
                    assert(resultUnit(model2.sendMove(move)))
                    assert(resultUnit(model3.receiveMove(move)))
                    assert(resultUnit(model.receiveMove(move)))
                    model2.mockNetworkDoneSending()
                    move = getRandomPlayerCard(model.listOfPlayers[2])
                    assert(resultUnit(model.receiveMove(move)))
                    assert(resultUnit(model2.receiveMove(move)))
                    assert(resultUnit(model3.sendMove(move)))
                    model3.mockNetworkDoneSending()
                    move = getRandomPlayerCard(model.listOfPlayers[0])
                    assert(resultUnit(model.sendMove(move)))
                    assert(resultUnit(model2.receiveMove(move)))
                    assert(resultUnit(model3.receiveMove(move)))
                    model.mockNetworkDoneSending()
                }
            }
        }
    }

    private fun getRandomPlayerCard(player: GameModelPlayer): String {
        var card = ""
        val dealer = GameModelCardDealer()
        while (!player.cardsContain(card)) {
            val newCard = dealer.dealCardInSet(player.id)
            card = newCard.getString()
        }
        return card
    }

    @Test
    fun checkLegalConfigTest() {
        var model = GameModel()

        assertFalse(resultUnit(model.receiveMove(buildString {
            append(2.toChar())
        })))


        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(0.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(2.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(4.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(6.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(2.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(6.toChar())
            append(1.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(7.toChar())
            append(5.toChar())
            append(0.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(7.toChar())
            append(1.toChar())
            append(0.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(7.toChar())
            append(1.toChar())
            append(0.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(15.toChar())
            append(0.toChar())
            append(1.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(15.toChar())
            append(0.toChar())
            append(1.toChar())
            append(15.toChar())
            append(0.toChar())
            append(2.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(15.toChar())
            append(0.toChar())
            append(1.toChar())
            append(15.toChar())
            append(0.toChar())
            append(2.toChar())
            append(15.toChar())
            append(0.toChar())
            append(3.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(15.toChar())
            append(0.toChar())
            append(1.toChar())
            append(15.toChar())
            append(0.toChar())
            append(2.toChar())
            append(15.toChar())
            append(0.toChar())
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(4.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(15.toChar())
            append(0.toChar())
            append(1.toChar())
            append(15.toChar())
            append(0.toChar())
            append(2.toChar())
            append(15.toChar())
            append(0.toChar())
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(4.toChar())
            append(15.toChar())
            append(0.toChar())
            append(5.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(15.toChar())
            append(0.toChar())
            append(1.toChar())
            append(15.toChar())
            append(0.toChar())
            append(2.toChar())
            append(15.toChar())
            append(0.toChar())
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(4.toChar())
            append(15.toChar())
            append(0.toChar())
            append(5.toChar())
            append(15.toChar())
            append(0.toChar())
            append(6.toChar())
        })))

        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(15.toChar())
            append(0.toChar())
            append(1.toChar())
            append(15.toChar())
            append(0.toChar())
            append(2.toChar())
            append(15.toChar())
            append(0.toChar())
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(4.toChar())
            append(15.toChar())
            append(0.toChar())
            append(5.toChar())
            append(15.toChar())
            append(0.toChar())
            append(6.toChar())
            append(15.toChar())
            append(0.toChar())
            append(7.toChar())
        })))


        model = GameModel()
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(0.toChar())
            append(15.toChar())
            append(0.toChar())
            append(1.toChar())
            append(15.toChar())
            append(0.toChar())
            append(2.toChar())
            append(15.toChar())
            append(0.toChar())
            append(3.toChar())
            append(15.toChar())
            append(0.toChar())
            append(4.toChar())
            append(15.toChar())
            append(0.toChar())
            append(5.toChar())
            append(15.toChar())
            append(0.toChar())
            append(2.toChar())
        })))
    }

    @Test
    fun checkLegalMoveTest() {
        val model = GameModel()
        val config = GameModelConfig(model)
        val model2 = GameModel()
        val model3 = GameModel()

        config.createConfig(3, 0)
        when (val conf = config.getConfig()) {
            is GameModelResult.Failure -> {
                assert(false) //should not happen
            }
            is GameModelResult.Success -> {
                assert(resultUnit(model2.receiveMove(conf.output)))
            }
        }
        when (val conf = config.getConfig()) {
            is GameModelResult.Failure -> {
                assert(false) //should not happen
            }
            is GameModelResult.Success -> {
                assert(resultUnit(model3.receiveMove(conf.output)))
            }
        }

        var move = getRandomPlayerCard(model.listOfPlayers[0])

        move = if (move[0] == 0.toChar()) {
            buildString {
                append(15)
                append(move[1])
                append(move[2])
            }
        } else {
            buildString {
                append((move[0].code - 1).toChar())
                append(move[1])
                append(move[2])
            }
        }

        assertFalse(resultUnit(model.receiveMove(move)))
    }

    @Test
    fun checkLegalMessageCardTest() {
        val model = GameModel()
        val config = GameModelConfig(model)

        config.createConfig(3, 0)
        assertFalse(
            model.legalMessageCard(
                buildString {
                    append(0.toChar())
                    append(0.toChar())
                    append(4.toChar())
                },
                0
            )
        )

        assertFalse(
            model.legalMessageCard(
                buildString {
                    append(0.toChar())
                    append(5.toChar())
                    append(6.toChar())
                },
                0
            )
        )
    }

    @Test
    fun receiveMoveTest() {
        var model = GameModel()
        var config = GameModelConfig(model)

        config.createConfig(3, 0)
        assertFalse(resultUnit(model.receiveMove(buildString {
            append(15.toChar())
            append(0.toChar())
            append(4.toChar())
        }
        )))

        assertFalse(resultUnit(model.receiveMove(buildString {
            append(0.toChar())
            append(5.toChar())
            append(6.toChar())
        }
        )))

        assertFalse(resultUnit(model.receiveMove(buildString {
            append(1.toChar())
            append(0.toChar())
            append(6.toChar())
        }
        )))

        assertFalse(resultUnit(model.receiveMove(buildString {
            append(1.toChar())
            append(0.toChar())
            append(6.toChar())
        }
        )))

        var move = ""
        assertFalse(resultUnit(model.receiveMove(move)))

        model = GameModel()
        config = GameModelConfig(model)

        config.createConfig(3, 0)
        move = getRandomPlayerCard(model.listOfPlayers[0])
        move = buildString {
            if (move[0].code == 0) {
                append(15.toChar())
                append(move[1])
                append(move[2])
            } else {
                append((move[0].code - 1).toChar())
                append(move[1])
                append(move[2])
            }
        }


        assertFalse(resultUnit(model.receiveMove(move)))

        model = GameModel()
        config = GameModelConfig(model)

        config.createConfig(3, 0)
        move = getRandomPlayerCard(model.listOfPlayers[0])
        move = buildString {
            if (move[0].code == 0) {
                append(15.toChar())
                append(move[1])
                append(move[2])
            } else {
                append((move[0].code - 1).toChar())
                append(move[1])
                append(move[2])
            }
        }


        assertFalse(resultUnit(model.sendMove(move)))

        model = GameModel()

        assertFalse(resultUnit(model.receiveMove(buildString {
            append(1.toChar())
        })))

    }
}