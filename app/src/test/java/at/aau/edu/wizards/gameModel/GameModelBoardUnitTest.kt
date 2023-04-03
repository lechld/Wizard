package at.aau.edu.wizards.gameModel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test


class GameModelBoardUnitTest {

    private val test = GameModelUnitTest()

    @Test
    fun testTrump() {
        val board: GameModelBoardInterface = GameModelBoard()

        assertFalse(test.resultInt(board.getTrump()))

        val legalValues = ArrayList<Char>()
        val illegalValues = ArrayList<Char>()

        for (value in 0..14) {
            legalValues.add(value.toChar())
        }
        illegalValues.add(15.toChar())

        val legalColors = ArrayList<Char>()
        val illegalColors = ArrayList<Char>()

        for (color in 1..4) {
            legalColors.add(color.toChar())
        }
        illegalColors.add(5.toChar())


        for (value in legalValues) {
            for (color in illegalColors) {
                assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                    append(value)
                    append(color)
                })))
            }
        }
        for (value in illegalValues) {
            for (color in legalColors) {
                assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                    append(value)
                    append(color)
                })))
            }
        }
        for (value in illegalValues) {
            for (color in illegalColors) {
                assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                    append(value)
                    append(color)
                })))
            }
        }

        for (value in legalValues) {
            for (color in illegalColors) {
                assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                    append(value)
                    append(color)
                    append(value)
                })))
            }
        }
        for (value in illegalValues) {
            for (color in legalColors) {
                assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                    append(value)
                    append(color)
                    append(value)
                })))
            }
        }
        for (value in illegalValues) {
            for (color in illegalColors) {
                assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                    append(value)
                    append(color)
                    append(value)
                })))
            }
        }
        for (value in legalValues) {
            for (color in legalColors) {
                assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                    append(value)
                    append(color)
                    append(value)
                })))
            }
        }
        for (color in legalColors) {
            assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                append(color)
            })))
        }
        for (color in illegalColors) {
            assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                append(color)
            })))
        }
        for (value in illegalValues) {
            assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                append(value)
            })))
        }
        for (value in legalValues) {
            assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
                append(value)
            })))
        }
        assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
            append((-1).toChar())
            append(0.toChar())
        })))
        assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
            append(0.toChar())
            append((-1).toChar())
        })))
        assertFalse(test.resultUnit(board.addTrumpToStack(buildString {
            append((-1).toChar())
            append((-1).toChar())
        })))


        assertFalse(test.resultUnit(board.nextTrump()))

        for (value in legalValues) {
            for (color in legalColors) {
                assert(test.resultUnit(board.addTrumpToStack(buildString {
                    append(value)
                    append(color)
                })))
                assert(test.resultUnit(board.nextTrump()))
                assert(test.resultInt(board.getTrump()))
            }
        }

        assert(test.resultUnit(board.addTrumpToStack(buildString {
            append(0.toChar())
            append(0.toChar())
        })))
        assert(test.resultUnit(board.nextTrump()))
        assert(test.resultInt(board.getTrump()))

        assertFalse(test.resultUnit(board.nextTrump()))
    }

    @Test
    fun testCards() {
        val parent = GameModel()
        parent.listOfPlayers.add(GameModelPlayer(0, 0))
        parent.listOfPlayers.add(GameModelPlayer(1, 0))
        val board: GameModelBoardInterface = GameModelBoard()

        assertFalse(test.resultGameModelCard(board.getWinningCard()))

        for (player in 0..10) {
            assertEquals(0, board.gamesWon(player))
        }

        val legalValues = ArrayList<Char>()
        val illegalValues = ArrayList<Char>()

        for (value in 0..14) {
            legalValues.add(value.toChar())
        }
        illegalValues.add(15.toChar())

        val legalColors = ArrayList<Char>()
        val illegalColors = ArrayList<Char>()

        for (color in 1..4) {
            legalColors.add(color.toChar())
        }
        illegalColors.add(5.toChar())
        illegalColors.add(0.toChar())

        for (value in illegalValues) {
            for (color in legalColors) {
                assertFalse(test.resultUnit(board.addWinningCard(GameModelCard(
                    buildString {
                        append(value)
                        append(color)
                        append(0.toChar())
                    }
                ))))
                assertFalse(test.resultUnit(board.addNonWinningCard(GameModelCard(
                    buildString {
                        append(value)
                        append(color)
                        append(0.toChar())
                    }
                ))))
            }
        }

        for (value in legalValues) {
            for (color in illegalColors) {
                assertFalse(test.resultUnit(board.addWinningCard(GameModelCard(
                    buildString {
                        append(value)
                        append(color)
                        append(0.toChar())
                    }
                ))))
                assertFalse(test.resultUnit(board.addNonWinningCard(GameModelCard(
                    buildString {
                        append(value)
                        append(color)
                        append(0.toChar())
                    }
                ))))
            }
        }

        for (value in illegalValues) {
            for (color in illegalColors) {
                assertFalse(test.resultUnit(board.addWinningCard(GameModelCard(
                    buildString {
                        append(value)
                        append(color)
                        append(0.toChar())
                    }
                ))))
                assertFalse(test.resultUnit(board.addNonWinningCard(GameModelCard(
                    buildString {
                        append(value)
                        append(color)
                        append(0.toChar())
                    }
                ))))
            }
        }

        for (value in legalValues) {
            for (color in legalColors) {
                assert(test.resultUnit(board.addWinningCard(GameModelCard(
                    buildString {
                        append(value)
                        append(color)
                        append(0.toChar())
                    }
                ))))
                assert(test.resultUnit(board.addNonWinningCard(GameModelCard(
                    buildString {
                        append(value)
                        append(color)
                        append(1.toChar())
                    }
                ))))
            }
        }
        assertEquals(0, board.gamesWon(0))
        assertEquals(0, board.gamesWon(1))
        board.clearGame()
        assertEquals(1, board.gamesWon(0))
        assertEquals(0, board.gamesWon(1))

        assert(test.resultUnit(board.addWinningCard(GameModelCard(
            buildString {
                append(0.toChar())
                append(1.toChar())
                append(0.toChar())
            }
        ))))

        assert(test.resultUnit(board.addWinningCard(GameModelCard(
            buildString {
                append(0.toChar())
                append(1.toChar())
                append(1.toChar())
            }
        ))))

        assertEquals(1, board.gamesWon(0))
        assertEquals(0, board.gamesWon(1))
        assert(test.resultGameModelCard(board.getWinningCard()))
        board.clearGame()
        assertFalse(test.resultGameModelCard(board.getWinningCard()))
        assertEquals(1, board.gamesWon(0))
        assertEquals(1, board.gamesWon(1))
        board.clearGame()
        assertEquals(1, board.gamesWon(0))
        assertEquals(1, board.gamesWon(1))

        assert(test.resultUnit(board.addWinningCard(GameModelCard(
            buildString {
                append(0.toChar())
                append(1.toChar())
                append(0.toChar())
            }
        ))))

        assert(test.resultUnit(board.addWinningCard(GameModelCard(
            buildString {
                append(0.toChar())
                append(1.toChar())
                append(1.toChar())
            }
        ))))

        val card = GameModelCard(
            buildString {
                append(0.toChar())
                append(1.toChar())
                append(0.toChar())
            }
        )
        assert(test.resultUnit(board.addWinningCard(card)))

        assert(test.resultUnit(board.addNonWinningCard(GameModelCard(
            buildString {
                append(14.toChar())
                append(1.toChar())
                append(1.toChar())
            }
        ))))

        when (val winningCard = board.getWinningCard()) {
            is GameModelResult.Failure -> {
                assert(false) //should not result in failure
            }
            is GameModelResult.Success -> {
                assertEquals(card, winningCard.output)
            }
        }
        assertEquals(1, board.gamesWon(0))
        assertEquals(1, board.gamesWon(1))
        board.clearGame()
        assertEquals(2, board.gamesWon(0))
        assertEquals(1, board.gamesWon(1))
        board.clearRound()
        assertEquals(0, board.gamesWon(0))
        assertEquals(0, board.gamesWon(1))
        assertFalse(test.resultGameModelCard(board.getWinningCard()))
    }

    @Test
    fun getTrumpCantBeNullTest() {
        val board: GameModelBoardInterface = GameModelBoard()
        assert(board.getTrumpCantBeNull() == 0)
        assert(test.resultUnit(board.addTrumpToStack(buildString {
            append(1.toChar())
            append(4.toChar())
        })))
        assert(test.resultUnit(board.nextTrump()))
        assert(board.getTrumpCantBeNull() == 4)
    }
}