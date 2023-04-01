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
        when (val winningCard = board.getWinningCard()) {
            is GameModelResult.Failure -> {
                if (currentPlayer == id && parent.listOfPlayers[id].cardsContain(move)) {
                    return true
                }
            }
            is GameModelResult.Success -> {
                if (currentPlayer == id && parent.listOfPlayers[id].cardsContain(move) && (move[0].code !in 1..13 || winningCard.output.value !in 1..13 || winningCard.output.color == move[1].code || !(parent.listOfPlayers[id].cardsContainColor(winningCard.output.color)))) {
                    return true
                }
            }
        }
        return false
    }

    override fun checkMoveLegalCheat(move: String): Boolean {
        if (!checkMoveLegal(move) && !parent.legalMessageCard(move, parent.listOfPlayers.size)) {
            when (board.getWinningCard()) {
                is GameModelResult.Failure -> {
                    return false
                }
                is GameModelResult.Success -> {
                    if (currentPlayer == id && parent.listOfPlayers[id].cardsContain(move)) {
                        return true
                    }
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
        when (val winningCard = board.getWinningCard()) {
            is GameModelResult.Failure -> {
                return GameModelResult.Success(true)
            }
            is GameModelResult.Success -> {
                when (val trump = board.getTrump()) {
                    is GameModelResult.Failure -> {
                        return GameModelResult.Failure(trump.throwable)
                    }
                    is GameModelResult.Success -> {
                        if (parent.listOfPlayers[id].cardsContain(move) && (move[0].code !in 1..13 || winningCard.output.value !in 1..13 || winningCard.output.color == move[1].code || !(parent.listOfPlayers[id].cardsContainColor(trump.output)))) {
                            return GameModelResult.Success(true)
                        }
                    }
                }
            }
        }
        return GameModelResult.Success(false)
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
        when (val forErrorHandling = dealCards()) {
            is GameModelResult.Failure -> {
                return GameModelResult.Failure(forErrorHandling.throwable)
            }
            is GameModelResult.Success -> {

            }
        }
        when (val forErrorHandling = board.nextTrump()) {
            is GameModelResult.Failure -> {
                return GameModelResult.Failure(forErrorHandling.throwable)
            }
            is GameModelResult.Success -> {

            }
        }
        when (val forErrorHandling = parent.listOfPlayers[id].getGuess()) {
            is GameModelResult.Failure -> {
                return GameModelResult.Failure(forErrorHandling.throwable)
            }
            is GameModelResult.Success -> {

            }
        }
        return GameModelResult.Success(Unit)
    }

    /**
     * Deals all players their respective cards.
     * Failure if it failed to deal cards for one player.
     */
    private fun dealCards(): GameModelResult<Unit> {
        for (player in parent.listOfPlayers) {
            when (val forErrorHandling = player.dealHand(turn)) {
                is GameModelResult.Failure -> {
                    return GameModelResult.Failure(forErrorHandling.throwable)
                }
                is GameModelResult.Success -> {

                }
            }
        }
        return GameModelResult.Success(Unit)
    }

    override fun playCard(move: String): GameModelResult<Unit> {
        if (!parent.legalMessageCard(move, parent.listOfPlayers.size) || !(currentPlayer == move[2].code && parent.listOfPlayers[move[2].code].cardsContain(move))) {
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
            }
        }
        when (val winningCard = board.getWinningCard()) {
            is GameModelResult.Failure -> {
                board.addWinningCard(GameModelCard(move, parent))
            }
            is GameModelResult.Success -> {
                if (winningCard.output.value == 14 || move[0].code == 0) {
                    board.addNonWinningCard(GameModelCard(move, parent))
                } else if (move[0].code == 14 || winningCard.output.value == 0) {
                    board.addWinningCard(GameModelCard(move, parent))
                } else if (move[1].code == winningCard.output.color) {
                    if (move[0].code > winningCard.output.value) {
                        board.addWinningCard(GameModelCard(move, parent))
                    } else {
                        board.addNonWinningCard(GameModelCard(move, parent))
                    }
                } else {
                    when (val trump = board.getTrump()) {
                        is GameModelResult.Failure -> {
                            return GameModelResult.Failure(trump.throwable)
                        }
                        is GameModelResult.Success -> {
                            if (move[1].code == trump.output) {
                                board.addWinningCard(GameModelCard(move, parent))
                            } else {
                                board.addNonWinningCard(GameModelCard(move, parent))
                            }
                        }
                    }
                }
            }
        }
        parent.listOfPlayers[move[2].code].removeCardFromHand(GameModelCard(move, parent))
        if (++currentPlayer == dealer) {
            dealer++
            currentPlayer++
            if (parent.listOfPlayers[0].cardsEmpty()) {
                if (turn == 10) {
                    //TODO CALL END OF GAME SCREEN
                } else {
                    for (player in parent.listOfPlayers) {
                        when (val forErrorHandling = player.score(board.gamesWon(player.id))) {
                            is GameModelResult.Failure -> {
                                return GameModelResult.Failure(forErrorHandling.throwable)
                            }
                            is GameModelResult.Success -> {

                            }
                        }
                    }
                    board.clearRound()
                    turn++
                    when (val forErrorHandling = dealCards()) {
                        is GameModelResult.Failure -> {
                            return GameModelResult.Failure(forErrorHandling.throwable)
                        }
                        is GameModelResult.Success -> {

                        }
                    }
                    when (val forErrorHandling = board.nextTrump()) {
                        is GameModelResult.Failure -> {
                            return GameModelResult.Failure(forErrorHandling.throwable)
                        }
                        is GameModelResult.Success -> {

                        }
                    }
                    when (val forErrorHandling = parent.listOfPlayers[id].getGuess()) {
                        is GameModelResult.Failure -> {
                            return GameModelResult.Failure(forErrorHandling.throwable)
                        }
                        is GameModelResult.Success -> {

                        }
                    }
                }
            } else {
                board.clearGame()
            }
        }
        return GameModelResult.Success(Unit)
    }

    override fun addTrump(trump: String): GameModelResult<Unit> {
        when (val forErrorHandling = board.addTrumpToStack(trump.substring(0, 1))) {
            is GameModelResult.Failure -> {
                return GameModelResult.Failure(forErrorHandling.throwable)
            }
            is GameModelResult.Success -> {

            }
        }
        return GameModelResult.Success(Unit)
    }
}