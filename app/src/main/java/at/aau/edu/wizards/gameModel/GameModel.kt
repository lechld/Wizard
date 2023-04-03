package at.aau.edu.wizards.gameModel

class GameModel : GameModelInterface {

    override val listOfPlayers = ArrayList<GameModelPlayer>()
    private val rules = GameModelRules(this)
    private var waitingForAnswer = false


    private val mockNetwork = ArrayList<String>()

    override fun sendMove(move: String): GameModelResult<Unit> {
        if (!legalMessageCard(
                move,
                listOfPlayers.size
            ) || (!rules.checkMoveLegal(move) && !rules.checkMoveLegalCheat(move))
        ) {
            return GameModelResult.Failure(Throwable("Unable to send move: No such move possible"))
        }
        if (!waitingForAnswer) {
            //Call real Network function - this is a mock implementation
            mockNetwork.add(move)
            waitingForAnswer = true
        } else {
            return GameModelResult.Failure(Throwable("Unable to send move: Already waiting for response!"))
        }
        return GameModelResult.Success(Unit)
    }

    override fun receiveMove(move: String): GameModelResult<Unit> {
        if (move.length == 3) {
            if (move[0].code < 15 && move[1].code < 4 && move[2].code == 6) {
                return when (val forErrorHandling = rules.addTrump(move)) {
                    is GameModelResult.Failure -> {
                        GameModelResult.Failure(forErrorHandling.throwable)
                    }
                    is GameModelResult.Success -> {
                        GameModelResult.Success(Unit)
                    }
                }
            } else if (!legalMessageCard(move, listOfPlayers.size)) {
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
        } else return if (move.isNotEmpty()) {
            if (!legalMessageConfig(move)) {
                return GameModelResult.Failure(Exception("Failed to load config: The string does not represent a legal config!"))
            }
            setConfig(move)
            GameModelResult.Success(Unit)
        } else {
            GameModelResult.Failure(Exception("Failed to load config: The string is empty!"))
        }
    }

    /**
     * Sets the config depending on the received String.
     */
    private fun setConfig(hash: String) {
        rules.id = hash[0].code
        for (pos in 0 until (hash.length - 1) / 3) {
            if (hash[pos * 3 + 1].code == 15) {
                listOfPlayers.add(GameModelPlayer(hash[pos * 3 + 3].code, hash[pos * 3 + 2].code))
            } else if (hash[pos * 3 + 3].code == 6) {
                rules.addTrump(
                    hash.substring(
                        pos * 3 + 1, pos * 3 + 4
                    )
                )
            } else {
                listOfPlayers[hash[pos * 3 + 3].code].addCardToPlayerStack(
                    GameModelCard(
                        hash.substring(
                            pos * 3 + 1, pos * 3 + 4
                        )
                    )
                )
            }
        }
        receiveMove(buildString { append(1.toChar()) })
    }

    override fun legalMessageCard(hash: String, numberOfPlayer: Int): Boolean {
        if (hash.length == 3 && hash[0].code < 15 && hash[1].code in 1..4 && (hash[2].code < numberOfPlayer || hash[2].code == 6) || (hash[0].code == 0 && hash[1].code == 0 && hash[2].code == 6)) {
            return true
        }
        return false
    }

    /**
     * Checks if the received message does represent a legal config value
     */
    private fun legalMessageConfig(hash: String): Boolean {
        if (hash[0].code in 1..5) {
            var playerCount = 0
            for (input in 0 until (hash.length - 1) / 3) {
                if ((hash[(input * 3) + 1].code == 15 && hash[(input * 3) + 2].code < 2 && hash[(input * 3) + 3].code < 6 && hash[(input * 3) + 3].code == playerCount)) {
                    playerCount++
                } else if (!legalMessageCard(
                        hash.substring((input * 3) + 1, (input * 3) + 4), playerCount
                    )
                ) {
                    return false
                }
            }
            if (playerCount > 2 && (56 * playerCount + 10) * 3 + 1 == hash.length) { //This is a simplified check, it does not guarantee the right split of cards between players and the right ratio of trumps to normal cards.
                return true
            }
        }
        return false
    }

    /**
     * This is a mock function! Delete and replace with real network once done!
     */
    fun mockNetworkDoneSending() {
        while (mockNetwork.isNotEmpty()) {
            receiveMove(mockNetwork[0])
            mockNetwork.removeAt(0)
        }
        waitingForAnswer = false
    }
}