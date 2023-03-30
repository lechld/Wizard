package at.aau.edu.wizards.gameModel

class GameModelRules(val parent: GameModel) : GameModelRulesInterface {
    var id = 0
    private val board = GameModelBoard()
    private val hasCheatedId = ArrayList<Int>()
    private var turn = 0
    private var dealer = 0
    private var currentPlayer = 0

    fun checkMoveLegal(move: String): Boolean {
        when (val winningCard = board.getWinningCard()) {
            is GameModelResult.Failure -> {
                if (currentPlayer == id && move[2].code == id && parent.listOfPlayers[id].cardsContain(move)) {
                    return true
                }
            }
            is GameModelResult.Success -> {
                if (currentPlayer == id && move[2].code == id && parent.listOfPlayers[id].cardsContain(move) && (move[0].code !in 1..13 || winningCard.output.value !in 1..13 || winningCard.output.color == move[1].code || !(parent.listOfPlayers[id].cardsContainColor(board.getTrump())))) {
                    return true
                }
            }
        }
        return false
    }

    fun checkMoveLegalCheat(move: String): Boolean {
        if (!checkMoveLegal(move)) {
            when (val winningCard = board.getWinningCard()) {
                is GameModelResult.Failure -> {
                    return false
                }
                is GameModelResult.Success -> {
                    if (currentPlayer == id && move[2].code == id && parent.listOfPlayers[id].cardsContain(move)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Checks if a move is a legal or legalCheat move. Cannot process illegal moves!
     * Returns true for legal, false for legalCheat.
     */
    private fun checkIsMoveLegalOrCheat(move: String): Boolean {
        when (val winningCard = board.getWinningCard()) {
            is GameModelResult.Failure -> {
                return true
            }
            is GameModelResult.Success -> {
                if (parent.listOfPlayers[id].cardsContain(move) && (move[0].code !in 1..13 || winningCard.output.value !in 1..13 || winningCard.output.color == move[1].code || !(parent.listOfPlayers[id].cardsContainColor(board.getTrump())))) {
                    return true
                }
            }
        }
        return false
    }

    fun isTurn(id: Int): Boolean {
        //TODO rename to isActivePlayer for better clarification
        if (id == currentPlayer) {
            return true
        }
        return false
    }

    fun initFirstTurn() {
        //TODO ERROR HANDLING
        turn = 1
        dealCards()
        board.nextTrump()
        parent.listOfPlayers[id].getGuess()
    }

    private fun dealCards() {
        for (player in parent.listOfPlayers) {
            player.dealHand(turn) //TODO ERROR HANDLING
        }
    }

    fun playCard(move: String) {
        //TODO ERROR HANDLING
        if (!checkIsMoveLegalOrCheat(move)) {
            hasCheatedId.add(move[2].code)
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
                    }
                } else if (move[1].code == board.getTrump()) {
                    board.addWinningCard(GameModelCard(move, parent))
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
                        player.score(board.gamesWon(player.id))
                    }
                    board.clearGame()
                    turn++
                    dealCards()
                    board.nextTrump()
                    parent.listOfPlayers[id].getGuess()
                }
            } else {
                board.clearGame()
            }
        }
    }

    fun addTrump(trump: String) {
        //TODO ERROR HANDLING
        board.trumpStack.add(trump.substring(0, 1))
    }
}