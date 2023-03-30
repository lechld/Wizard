package at.aau.edu.wizards.gameModel

class GameModel() : GameModelInterface {

    val listOfPlayers = ArrayList<GameModelPlayer>()
    private val rules = GameModelRules(this)

    override fun sendMove(move: String, cheat: Boolean): GameModelResult<Unit> {
        if (!legalMessageCard(move) || !cheat && !rules.checkMoveLegal(move) || cheat && !rules.checkMoveLegalCheat(move)) {
            GameModelResult.Failure(Throwable("Unable to send move: No such move legally possible"))
        }
        TODO("Call Network function")
    }

    override fun receiveMove(move: String): GameModelResult<Unit> {
        if (move.length == 3) {
            if (move[0].code in 0..14 && move[1].code in 1..4 && move[2].code == 6) {
                rules.addTrump(move)
                return GameModelResult.Success<Unit>(Unit)
            } else if (!legalMessageCard(move) || !legalMove(move)) {
                return GameModelResult.Failure(Exception("Failed to load move: The string does not represent a legal move!"))
            }
            rules.playCard(move)
        } else if (move.length == 1 && move[0].code == 1) {
            rules.initFirstTurn()
        } else {
            if (!legalMessageConfig(move)) {
                return GameModelResult.Failure(Exception("Failed to load config: The string does not represent a legal config!"))
            }
            setConfig(move)
        }
        return GameModelResult.Success<Unit>(Unit)
    }

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

    /**
     * Checks if the received message does indeed represent a legal card value
     */
    private fun legalMessageCard(hash: String): Boolean {
        if (hash[0].code in 0..14 && hash[1].code in 1..4 && hash[2].code in 0 until listOfPlayers.size) {
            return true
        }
        return false
    }

    /**
     * Checks if the received message does indeed represent a legal config value
     */
    private fun legalMessageConfig(hash: String): Boolean {
        if (hash[0].code in 1..5) {
            var playerCount = 0
            for (input in 1..(hash.length - 1) / 3) {
                if ((hash[input].code != 15 || hash[input + 1].code !in 0..1 || hash[input + 2].code !in 0..5)) {
                    playerCount++
                } else if (!legalMessageCard(hash.substring(input, input + 2))) {
                    return false
                }
            }
            if (playerCount !in 3..6 || 55 * playerCount + 1 != hash.length) {
                return false
            }
            return true
        }
        return false
    }

    private fun legalMove(move: String): Boolean {
        if (listOfPlayers[move[2].code].cardsContain(move) && rules.isTurn(move[2].code)) {
            return true
        }
        return false
    }
}