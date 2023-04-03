package at.aau.edu.wizards.gameModel

class GameModelRules(private val parent: GameModel) : GameModelRulesInterface {
    var id = 0
    private val board = GameModelBoard()
    private val hasCheatedId = ArrayList<Int>()
    private var turn = 0
    private var dealer = 0
    private var currentPlayer = 0

    override fun checkMoveLegal(move: String): Boolean {
        if (!parent.legalMessageCard(move, parent.listOfPlayers.size)) {
            return false
        }
        return when (val winningCard = board.getWinningCard()) {
            is GameModelResult.Failure -> {
                if (currentPlayer == id &&
                    id == move[2].code
                    && parent.listOfPlayers[id].cardsContain(move)
                ) {
                    return true
                }
                false
            }
            is GameModelResult.Success -> {
                if (currentPlayer == id
                    &&
                    id == move[2].code
                    && parent.listOfPlayers[id].cardsContain(move)
                    && (move[0].code !in 1..13
                            || winningCard.output.value !in 1..13
                            || winningCard.output.color == move[1].code
                            || !(parent.listOfPlayers[id].cardsContainColor(
                        winningCard.output.color
                    )))
                ) {
                    return true
                }
                false
            }
        }
    }

    override fun checkMoveLegalCheat(move: String): Boolean {
        if (!checkMoveLegal(move) && parent.legalMessageCard(move, parent.listOfPlayers.size)) {
            return when (board.getWinningCard()) {
                is GameModelResult.Failure -> {
                    false
                }
                is GameModelResult.Success -> {
                    if (currentPlayer == id
                        && parent.listOfPlayers[id].cardsContain(move)
                    ) {
                        return true
                    }
                    false
                }
            }
        }
        return false
    }

    /**
     * Checks if a move is either a legal or legalCheat move.
     * Returns true for legal, false for legalCheat.
     * !Be careful when using, as it cannot process illegal moves improper usage can lead to program termination!
     * This does not check if the local player is performing the move.
     */
    private fun checkIsMoveLegalOrCheat(move: String): GameModelResult<Boolean> {

        return when (val trump = board.getTrump()) {
            is GameModelResult.Failure -> {
                GameModelResult.Failure(trump.throwable)
            }
            is GameModelResult.Success -> {
                when (val winningCard = board.getWinningCard()) {
                    is GameModelResult.Failure -> {
                        GameModelResult.Success(true)
                    }
                    is GameModelResult.Success -> {
                        if (move[0].code !in 1..13
                            || winningCard.output.value !in 1..13
                            || winningCard.output.color == move[1].code
                            || !(parent.listOfPlayers[id].cardsContainColor(
                                trump.output
                            ))
                        ) {
                            GameModelResult.Success(true)
                        } else {
                            GameModelResult.Success(false)
                        }
                    }
                }
            }
        }
    }

    override fun isActivePlayer(id: Int): Boolean {
        if (id == currentPlayer) {
            return true
        }
        return false
    }

    override fun initFirstRound(): GameModelResult<Unit> {
        if (turn != 0) {
            return GameModelResult.Failure(Exception("Unable to initialize game: Game was already initialized!"))
        }
        turn = 1
        if (id >= parent.listOfPlayers.size) {
            return GameModelResult.Failure(Exception("Unable to initialize game: Invalid id set for this model!"))
        }
        parent.listOfPlayers[id].getGuess()
        return when (val forErrorHandling = dealCards()) {
            is GameModelResult.Failure -> {
                GameModelResult.Failure(forErrorHandling.throwable)
            }
            is GameModelResult.Success -> {
                when (val forErrorHandlingExtra = board.nextTrump()) {
                    is GameModelResult.Failure -> {
                        GameModelResult.Failure(forErrorHandlingExtra.throwable)
                    }
                    is GameModelResult.Success -> {
                        GameModelResult.Success(Unit)
                    }
                }
            }
        }
    }

    /**
     * Deals all players their respective cards.
     * Failure if it failed to deal cards for one player.
     */
    private fun dealCards(): GameModelResult<Unit> {
        for (player in parent.listOfPlayers) {
            return when (val forErrorHandling = player.dealHand(turn)) {
                is GameModelResult.Failure -> {
                    GameModelResult.Failure(forErrorHandling.throwable)
                }
                is GameModelResult.Success -> {
                    continue
                }
            }
        }
        return GameModelResult.Success(Unit)
    }

    override fun playCard(move: String): GameModelResult<Unit> {
        if (!parent.legalMessageCard(
                move,
                parent.listOfPlayers.size
            ) || currentPlayer != move[2].code || !parent.listOfPlayers[move[2].code].cardsContain(
                move
            )
        ) {
            return GameModelResult.Failure(Throwable("Failed to play card: Player does not own card and/or is not his/her turn!"))
        }
        when (val forErrorHandling = checkIsMoveLegalOrCheat(move)) {
            is GameModelResult.Failure -> {
                return GameModelResult.Failure(forErrorHandling.throwable)
            }
            is GameModelResult.Success -> {
                if (!forErrorHandling.output) {
                    hasCheatedId.add(move[2].code)
                }
                addCardToBoard(move)
                parent.listOfPlayers[move[2].code].removeCardFromHand(GameModelCard(move))
                if (++currentPlayer == parent.listOfPlayers.size) {
                    currentPlayer = 0
                }
                if (currentPlayer == dealer) {
                    board.clearGame()
                    if (parent.listOfPlayers[0].cardsEmpty()) {
                        if (++dealer == parent.listOfPlayers.size) {
                            dealer = 0
                        }
                        if (++currentPlayer == parent.listOfPlayers.size) {
                            currentPlayer = 0
                        }
                        if (turn == 10) {
                            //TODO CALL END OF GAME SCREEN
                            return GameModelResult.Success(Unit)
                        } else {
                            for (player in parent.listOfPlayers) {
                                player.score(board.gamesWon(player.id))
                            }
                            parent.listOfPlayers[id].getGuess()
                            board.clearRound()
                            turn++
                            return when (val forErrorHandlingExtra = dealCards()) {
                                is GameModelResult.Failure -> {
                                    GameModelResult.Failure(forErrorHandlingExtra.throwable)
                                }
                                is GameModelResult.Success -> {
                                    when (val forErrorHandlingExtraExtra = board.nextTrump()) {
                                        is GameModelResult.Failure -> {
                                            GameModelResult.Failure(forErrorHandlingExtraExtra.throwable)
                                        }
                                        is GameModelResult.Success -> {
                                            GameModelResult.Success(Unit)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return GameModelResult.Success(Unit)
            }
        }
    }

    override fun addTrump(trump: String): GameModelResult<Unit> {
        return when (val forErrorHandling = board.addTrumpToStack(trump.substring(0, 2))) {
            is GameModelResult.Failure -> {
                GameModelResult.Failure(forErrorHandling.throwable)
            }
            is GameModelResult.Success -> {
                GameModelResult.Success(Unit)
            }
        }
    }

    /**
     * Adds a card to the board. Function was introduced for better coverage + better readability.
     */
    private fun addCardToBoard(move: String) {
        when (val winningCard = board.getWinningCard()) {
            is GameModelResult.Failure -> {
                board.addWinningCard(GameModelCard(move))
                return
            }
            is GameModelResult.Success -> {
                if (winningCard.output.value == 14 || move[0].code == 0) {
                    board.addNonWinningCard(GameModelCard(move))
                } else if (move[0].code == 14 || winningCard.output.value == 0) {
                    board.addWinningCard(GameModelCard(move))
                } else if (move[1].code == winningCard.output.color) {
                    if (move[0].code > winningCard.output.value) {
                        board.addWinningCard(GameModelCard(move))
                    } else {
                        board.addNonWinningCard(GameModelCard(move))
                    }
                } else {
                    if (board.getTrumpCantBeNull() == move[1].code) {
                        board.addWinningCard(GameModelCard(move))
                    } else {
                        board.addNonWinningCard(GameModelCard(move))
                    }
                }
                return
            }
        }
    }

    override fun getCurrentPlayer(): Int {
        return currentPlayer
    }

    override fun getActiveTrump(): GameModelCard {
        return when (val trump = board.getTrumpComplete()) {
            is GameModelResult.Failure -> {
                GameModelCard(buildString {
                    append(0.toChar())
                    append(0.toChar())
                    append(0.toChar())
                })
            }
            is GameModelResult.Success -> {
                trump.output
            }
        }
    }
}