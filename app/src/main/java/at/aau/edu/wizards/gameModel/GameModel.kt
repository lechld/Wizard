package at.aau.edu.wizards.gameModel

class GameModel : GameModelInterface {

    override val listOfPlayers = ArrayList<GameModelPlayer>()
    private val rules = GameModelRules(this)

    override fun sendMove(move: String): GameModelResult<Unit> {
        if (!legalMessageCard(move, listOfPlayers.size) || (!rules.checkMoveLegal(move) && !rules.checkMoveLegalCheat(move))) {
            return GameModelResult.Failure(Throwable("Unable to send move: No such move possible"))
        }
        //TODO("Call Network function")
        //for mock implementation we will just call local receiveMove function
        return when (val forErrorHandling = receiveMove(move)) {
            is GameModelResult.Failure -> {
                GameModelResult.Failure(forErrorHandling.throwable)
            }
            is GameModelResult.Success -> {
                GameModelResult.Success(Unit)
            }
        }
    }

    override fun receiveMove(move: String): GameModelResult<Unit> {
        if (move.length == 3) {
            if (move[0].code in 0..14 && move[1].code in 0..4 && move[2].code == 6) {
                return when (val forErrorHandling = rules.addTrump(move)) {
                    is GameModelResult.Failure -> {
                        GameModelResult.Failure(forErrorHandling.throwable)
                    }
                    is GameModelResult.Success -> {
                        GameModelResult.Success(Unit)
                    }
                }
            } else if (!legalMessageCard(move, listOfPlayers.size) || !legalMove(move)) {
                return GameModelResult.Failure(Exception("Failed to load move: The string does not represent a legal move!"))
            }
            return when (val forErrorHandling = rules.playCard(move)) {
                is GameModelResult.Failure -> {
                    GameModelResult.Failure(forErrorHandling.throwable)
                }
                is GameModelResult.Success -> {
                    GameModelResult.Success(Unit)
                }
            }
        } else if (move.length == 1 && move[0].code == 1) {
            return when (val forErrorHandling = rules.initFirstRound()) {
                is GameModelResult.Failure -> {
                    GameModelResult.Failure(forErrorHandling.throwable)
                }
                is GameModelResult.Success -> {
                    GameModelResult.Success(Unit)
                }
            }
        } else {
            if (!legalMessageConfig(move)) {
                return GameModelResult.Failure(Exception("Failed to load config: The string does not represent a legal config!"))
            }
            setConfig(move)
            return GameModelResult.Success(Unit)
        }
    }

    /**
     * Sets the config depending on the received String.
     */
    private fun setConfig(hash: String) {
        rules.id = hash[0].code
        for (pos in 1..(hash.length - 1) / 3) {
            if (hash[pos].code == 15) {
                listOfPlayers.add(GameModelPlayer(hash[pos + 2].code, hash[pos + 1].code))
            } else if (hash[pos + 2].code == 6) {
                rules.addTrump(hash)
            } else {
                listOfPlayers[hash[pos + 2].code].cards.add(GameModelCard(hash.substring(pos, pos + 2), this))
            }
        }
        receiveMove(buildString { append(1.toChar()) })
    }

    override fun legalMessageCard(hash: String, numberOfPlayer: Int): Boolean {
        if (hash[0].code in 0..14 && hash[1].code in 1..4 && hash[2].code in 0 until numberOfPlayer) {
            return true
        }
        return false
    }

    /**
     * Checks if the received message does represent a legal config value
     */
    private fun legalMessageConfig(hash: String): Boolean {
        if (hash.length != 1 && hash[0].code in 1..5) {
            var playerCount = 0
            for (input in 1..(hash.length - 1) / 3) {
                if ((hash[input].code == 15 || hash[input + 1].code !in 0..1 || hash[input + 2].code !in 0..5)) {
                    playerCount++
                } else if (!legalMessageCard(hash.substring(input, input + 2), playerCount)) {
                    return false
                }
            }
            if (playerCount in 3..6 && ((55 * playerCount + 1) + 10) * 3 == hash.length) {
                return true
            }
        }
        return false
    }

    /**
     * Checks if a message is a legal move.
     */
    private fun legalMove(move: String): Boolean {
        if (listOfPlayers[move[2].code].cardsContain(move) && rules.isActivePlayer(move[2].code)) {
            return true
        }
        return false
    }
}