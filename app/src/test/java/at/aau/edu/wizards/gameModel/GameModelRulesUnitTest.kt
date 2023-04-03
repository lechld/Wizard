package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class GameModelRulesUnitTest {
    private val test = GameModelUnitTest()

    @Test
    fun checkMoveTest() {
        var parent = GameModel()
        var rules = GameModelRules(parent)
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))

        var move =
            buildString {
                append(4.toChar())
                append(1.toChar())
                append(0.toChar())

            }

        assertFalse(rules.checkMoveLegal(move))
        assertFalse(rules.checkMoveLegalCheat(move))
        assertFalse(rules.isActivePlayer(1))
        assert(rules.isActivePlayer(0))

        assertFalse(test.resultUnit(rules.playCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
            append(0.toChar())
        })))

        assertFalse(test.resultUnit(rules.playCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        })))

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        assert(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(6.toChar())
                        append(1.toChar())
                        append(6.toChar())
                    },
                )
            )
        )

        assert(test.resultUnit(rules.initFirstRound()))

        assert(rules.checkMoveLegal(move))
        assertFalse(rules.checkMoveLegalCheat(move))
        assert(test.resultUnit(rules.playCard(move)))

        rules.id = 1
        var move2 = buildString {
            append(5.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        assertFalse(rules.checkMoveLegal(move2))
        assertFalse(rules.checkMoveLegalCheat(move2))
        assertFalse(test.resultUnit(rules.playCard(move2)))

        move2 = buildString {
            append(5.toChar())
            append(2.toChar())
            append(1.toChar())
        }
        assertFalse(rules.checkMoveLegal(move2))
        assertFalse(rules.checkMoveLegalCheat(move2))
        assertFalse(test.resultUnit(rules.playCard(move2)))

        move2 = buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        assert(rules.checkMoveLegal(move2))
        assertFalse(rules.checkMoveLegalCheat(move2))
        parent.listOfPlayers[1].getGuess()
        assertFalse(test.resultUnit(rules.playCard(move2)))

        //New
        parent = GameModel()
        rules = GameModelRules(parent)
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))


        assertFalse(rules.checkMoveLegal(move))
        assertFalse(rules.checkMoveLegalCheat(move))
        assertFalse(rules.isActivePlayer(1))
        assert(rules.isActivePlayer(0))


        assertFalse(test.resultUnit(rules.playCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        })))

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        assert(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(6.toChar())
                        append(1.toChar())
                        append(6.toChar())
                    },
                )
            )
        )

        assert(test.resultUnit(rules.initFirstRound()))

        assert(rules.checkMoveLegal(move))
        assertFalse(rules.checkMoveLegalCheat(move))
        assert(test.resultUnit(rules.playCard(move)))

        rules.id = 1

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(2.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(2.toChar())
            append(1.toChar())
        }
        ))
        assert(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(13.toChar())
                        append(2.toChar())
                        append(6.toChar())
                    },
                )
            )
        )

        assert(rules.checkMoveLegal(move2))
        assertFalse(rules.checkMoveLegalCheat(move2))
        parent.listOfPlayers[1].getGuess()
        assert(test.resultUnit(rules.playCard(move2)))

        rules.id = 1
        move = buildString {
            append(4.toChar())
            append(2.toChar())
            append(0.toChar())
        }
        move2 = buildString {
            append(4.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        assert(rules.checkMoveLegal(move2))
        assertFalse(rules.checkMoveLegalCheat(move2))
        assert(test.resultUnit(rules.playCard(move2)))

        assert(rules.id == 1)
        rules.id = 0
        assertFalse(rules.checkMoveLegal(move))
        assert(rules.checkMoveLegalCheat(move))
        assert(test.resultUnit(rules.playCard(move)))

        //deal 3rd round

        val cardr3p0a = buildString {

            append(4.toChar())
            append(1.toChar())
            append(0.toChar())

        }
        val cardr3p0b = buildString {

            append(14.toChar())
            append(1.toChar())
            append(0.toChar())

        }
        val cardr3p0c = buildString {

            append(0.toChar())
            append(1.toChar())
            append(0.toChar())

        }

        val cardr3p1a = buildString {

            append(3.toChar())
            append(1.toChar())
            append(1.toChar())

        }
        val cardr3p1b = buildString {

            append(0.toChar())
            append(1.toChar())
            append(1.toChar())

        }
        val cardr3p1c = buildString {

            append(14.toChar())
            append(1.toChar())
            append(1.toChar())

        }

        rules.addTrump(
            buildString {

                append(4.toChar())
                append(1.toChar())
                append(0.toChar())

            }
        )

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(cardr3p0a))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(cardr3p0b))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(cardr3p0c))

        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(cardr3p1a))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(cardr3p1b))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(cardr3p1c))

        move2 = buildString {
            append(5.toChar())
            append(2.toChar())
            append(1.toChar())
        }

        move = buildString {
            append(5.toChar())
            append(1.toChar())
            append(0.toChar())
        }

        rules.id = 1
        assert(rules.checkMoveLegal(move2))
        assertFalse(rules.checkMoveLegalCheat(move2))
        assert(test.resultUnit(rules.playCard(move2)))

        rules.id = 0
        assert(rules.checkMoveLegal(move))
        assertFalse(rules.checkMoveLegalCheat(move))
        for (player in parent.listOfPlayers) {
            player.getGuess()
        }
        assert(test.resultUnit(rules.playCard(move)))

        //Round 3
        rules.id = 0
        assert(rules.checkMoveLegal(cardr3p0a))
        assertFalse(rules.checkMoveLegalCheat(cardr3p0a))
        assert(test.resultUnit(rules.playCard(cardr3p0a)))

        rules.id = 1
        assert(rules.checkMoveLegal(cardr3p1a))
        assertFalse(rules.checkMoveLegalCheat(cardr3p1a))
        assert(test.resultUnit(rules.playCard(cardr3p1a)))

        rules.id = 0
        assert(rules.checkMoveLegal(cardr3p0b))
        assertFalse(rules.checkMoveLegalCheat(cardr3p0b))
        assert(test.resultUnit(rules.playCard(cardr3p0b)))

        rules.id = 1
        assert(rules.checkMoveLegal(cardr3p1b))
        assertFalse(rules.checkMoveLegalCheat(cardr3p1b))
        assert(test.resultUnit(rules.playCard(cardr3p1b)))
        //Give cards for round 4

        val cardr4p0a = buildString {

            append(4.toChar())
            append(1.toChar())
            append(0.toChar())

        }
        val cardr4p0b = buildString {

            append(0.toChar())
            append(1.toChar())
            append(0.toChar())

        }
        val cardr4p0c = buildString {

            append(0.toChar())
            append(1.toChar())
            append(0.toChar())

        }
        val cardr4p0d = buildString {

            append(6.toChar())
            append(1.toChar())
            append(0.toChar())

        }

        val cardr4p1a = buildString {

            append(0.toChar())
            append(1.toChar())
            append(1.toChar())

        }
        val cardr4p1b = buildString {

            append(0.toChar())
            append(1.toChar())
            append(1.toChar())

        }
        val cardr4p1c = buildString {

            append(14.toChar())
            append(1.toChar())
            append(1.toChar())

        }
        val cardr4p1d = buildString {

            append(14.toChar())
            append(1.toChar())
            append(1.toChar())

        }

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(cardr4p0a))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(cardr4p0b))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(cardr4p0c))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(cardr4p0d))

        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(cardr4p1a))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(cardr4p1b))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(cardr4p1c))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(cardr4p1d))

        rules.addTrump(
            buildString {

                append(0.toChar())
                append(4.toChar())
                append(6.toChar())

            }
        )

        //End round 3
        rules.id = 0
        assert(rules.checkMoveLegal(cardr3p0c))
        assertFalse(rules.checkMoveLegalCheat(cardr3p0c))
        assert(test.resultUnit(rules.playCard(cardr3p0c)))

        rules.id = 1
        assert(rules.checkMoveLegal(cardr3p1c))
        assertFalse(rules.checkMoveLegalCheat(cardr3p1c))
        assert(test.resultUnit(rules.playCard(cardr3p1c)))

        //Round 4

        rules.id = 1
        assert(rules.checkMoveLegal(cardr4p1c))
        assertFalse(rules.checkMoveLegalCheat(cardr4p1c))
        assert(test.resultUnit(rules.playCard(cardr4p1c)))

        rules.id = 0
        assert(rules.checkMoveLegal(cardr4p0c))
        assertFalse(rules.checkMoveLegalCheat(cardr4p0c))
        assert(test.resultUnit(rules.playCard(cardr4p0c)))

        rules.id = 1
        assert(rules.checkMoveLegal(cardr4p1b))
        assertFalse(rules.checkMoveLegalCheat(cardr4p1b))
        assert(test.resultUnit(rules.playCard(cardr4p1b)))

        rules.id = 0
        assert(rules.checkMoveLegal(cardr4p0b))
        assertFalse(rules.checkMoveLegalCheat(cardr4p0b))
        assert(test.resultUnit(rules.playCard(cardr4p0b)))

        rules.id = 1
        assert(rules.checkMoveLegal(cardr4p1a))
        assertFalse(rules.checkMoveLegalCheat(cardr4p1a))
        assert(test.resultUnit(rules.playCard(cardr4p1a)))

        rules.id = 0
        assert(rules.checkMoveLegal(cardr4p0a))
        assertFalse(rules.checkMoveLegalCheat(cardr4p0a))
        assert(test.resultUnit(rules.playCard(cardr4p0a)))

        //init rest of cards

        val cardsDealtPlayer0 = ArrayList<GameModelCard>()
        val cardsDealtPlayer1 = ArrayList<GameModelCard>()
        val dealer = GameModelCardDealer()

        var card: GameModelCard
        for (turn in 5..11) {
            for (game in 1..turn) {
                card = dealer.dealCardInSet(0)
                cardsDealtPlayer0.add(card)
                parent.listOfPlayers[0].addCardToPlayerStack(cardsDealtPlayer0[cardsDealtPlayer0.size - 1])
                card = dealer.dealCardInSet(1)
                cardsDealtPlayer1.add(card)
                parent.listOfPlayers[1].addCardToPlayerStack(cardsDealtPlayer1[cardsDealtPlayer1.size - 1])
            }
            card = dealer.dealCardInSet(0)
            rules.addTrump(card.getString())
            dealer.resetSet()
        }

        rules.id = 1
        assert(rules.checkMoveLegal(cardr4p1d))
        assertFalse(rules.checkMoveLegalCheat(cardr4p1d))
        assert(test.resultUnit(rules.playCard(cardr4p1d)))

        rules.id = 0
        assert(rules.checkMoveLegal(cardr4p0d))
        assertFalse(rules.checkMoveLegalCheat(cardr4p0d))
        assert(test.resultUnit(rules.playCard(cardr4p0d)))

        //now play till turn 10 so we can check end game function - we start with player 0 playing

        var pos = 0
        for (turn in 5..10) {
            for (game in 1..turn) {
                if (turn % 2 == 1) {
                    //player 0 starts
                    rules.id = 0
                    assert(test.resultUnit(rules.playCard(cardsDealtPlayer0[pos].getString())))
                    rules.id = 1
                    assert(test.resultUnit(rules.playCard(cardsDealtPlayer1[pos].getString())))
                } else {
                    //player 1 starts
                    rules.id = 1
                    assert(test.resultUnit(rules.playCard(cardsDealtPlayer1[pos].getString())))
                    rules.id = 0
                    assert(test.resultUnit(rules.playCard(cardsDealtPlayer0[pos].getString())))
                }
                pos++
            }
        }
    }

    @Test
    fun playWithNoTrumpTest() {
        val parent = GameModel()
        val rules = GameModelRules(parent)
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))

        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        assert(test.resultUnit(parent.listOfPlayers[0].dealHand(1)))
        assert(test.resultUnit(parent.listOfPlayers[1].dealHand(1)))

        val move = buildString {

            append(4.toChar())
            append(1.toChar())
            append(0.toChar())

        }
        assert(rules.checkMoveLegal(move))
        assertFalse(rules.checkMoveLegalCheat(move))
        assertFalse(test.resultUnit(rules.playCard(move)))


    }

    @Test
    fun checkInitTest() {
        var parent = GameModel()
        var rules = GameModelRules(parent)
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))

        assertFalse(test.resultUnit(rules.initFirstRound()))

        rules = GameModelRules(parent)

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))

        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))

        assertFalse(test.resultUnit(rules.initFirstRound()))


        parent = GameModel()
        rules = GameModelRules(parent)
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        assert(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(6.toChar())
                        append(1.toChar())
                        append(6.toChar())
                    },
                )
            )
        )
        assertFalse(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(15.toChar())
                        append(1.toChar())
                        append(6.toChar())
                    },
                )
            )
        )

        assert(test.resultUnit(rules.initFirstRound()))


        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(2.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(2.toChar())
            append(1.toChar())
        }
        ))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(3.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(3.toChar())
            append(1.toChar())
        }
        ))
        assert(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(6.toChar())
                        append(3.toChar())
                        append(6.toChar())
                    },
                )
            )
        )

        assertFalse(test.resultUnit(rules.initFirstRound()))
    }

    @Test
    fun noNextTrumpTest() {
        val parent = GameModel()
        val rules = GameModelRules(parent)
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        assert(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(6.toChar())
                        append(1.toChar())
                        append(6.toChar())
                    },
                )
            )
        )

        assert(test.resultUnit(rules.initFirstRound()))

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))

        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))

        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        rules.id = 0
        assert(test.resultUnit(rules.playCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        })))

        rules.id = 1
        assertFalse(test.resultUnit(rules.playCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        })))
    }

    @Test
    fun getNoTrump2Test() {
        val parent = GameModel()
        val rules = GameModelRules(parent)
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))

        rules.id = 0
        parent.listOfPlayers[0].dealHand(1)
        parent.listOfPlayers[1].dealHand(1)
        assertFalse(test.resultUnit(rules.playCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        })))
    }

    @Test
    fun checkMoveLegalTest() {
        val parent = GameModel()
        val rules = GameModelRules(parent)
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 1))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))

        assert(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(6.toChar())
                        append(1.toChar())
                        append(6.toChar())
                    },
                )
            )
        )

        assert(test.resultUnit(rules.initFirstRound()))

        assertFalse(rules.checkMoveLegal(buildString {
            append(6.toChar())
            append(1.toChar())
            append(6.toChar())
            append(4.toChar())
        }))

        rules.id = 0

        assertFalse(rules.checkMoveLegal(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }))

        rules.id = 1

        assertFalse(rules.checkMoveLegal(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }))

        rules.id = 0

        assert(rules.checkMoveLegal(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        }))

        assert(test.resultUnit(rules.playCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(0.toChar())
        })))

        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(1.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(1.toChar())
            append(1.toChar())
        }
        ))
        parent.listOfPlayers[0].addCardToPlayerStack(GameModelCard(buildString {
            append(4.toChar())
            append(2.toChar())
            append(0.toChar())
        }
        ))
        parent.listOfPlayers[1].addCardToPlayerStack(GameModelCard(buildString {
            append(5.toChar())
            append(2.toChar())
            append(1.toChar())
        }
        ))
        assert(
            test.resultUnit(
                rules.addTrump(
                    buildString {
                        append(13.toChar())
                        append(2.toChar())
                        append(6.toChar())
                    },
                )
            )
        )

        rules.id = 0
        assertFalse(rules.checkMoveLegal(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }))

        assertFalse(rules.checkMoveLegalCheat(buildString {
            append(15.toChar())
            append(1.toChar())
            append(1.toChar())
            append(1.toChar())
        }))

        assertFalse(rules.checkMoveLegalCheat(buildString {
            append(5.toChar())
            append(1.toChar())
            append(1.toChar())
        }))

    }
}