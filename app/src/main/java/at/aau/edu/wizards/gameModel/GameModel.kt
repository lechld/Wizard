package at.aau.edu.wizards.gameModel

class GameModel {

    private val players = ArrayList<GameModelPlayer>()
    private var dealer = GameModelDealer(0)
    private var rules = GameModelRules(players, 0, dealer)
    private var listener = GameModelListener(rules, players)

    fun sendMessage(move: String): GameModelResult<Unit> {
        if ((legalMessageGuess(move) && (move[0].code - 60) / 11 == rules.id) || (legalMessageCard(
                move
            ) && rules.localPlayerOwns(dealer.getCardFromHash(move[0].code)))
        ) {
            //TODO send to network
            return GameModelResult.Success(Unit)
        }
        return GameModelResult.Failure(Exception("Failed to send message: Not a legal guess/move!"))
    }

    fun receiveMessage(move: String) {
        if (players.isEmpty()) { //Check if game still needs to be initialized, if initialize it / exit
            if (move.length > 3) {
                init(move)
            } else {
                return
            }
        } else if (legalMessageCard(move)) { //Check if player is playing a card
            rules.playCard(dealer.getCardFromHash(move[0].code))
        } else if (legalMessageGuess(move)) {
            players[(move[0].code - 60) / 11].guesses.add((move[0].code - 60) % 11)
        }
        listener.update()
    }

    private fun legalMessageCard(move: String): Boolean {
        if (move.length == 1 && move[0].code < 60 && rules.currentPlayerOwns(
                dealer.getCardFromHash(
                    move[0].code
                )
            )
        ) {
            return true
        }
        return false
    }

    private fun legalMessageGuess(move: String): Boolean {
        if (move.length == 1 && move.length < (60 + players.size * 11)) {
            return true
        }
        return false
    }

    private fun init(move: String) {
        try {
            dealer = GameModelDealer(move.substring(3, move.length).toInt())
        } catch (error: NumberFormatException) {
            return
        }
        rules = GameModelRules(players, move[0].code, dealer)
        listener = GameModelListener(rules, players)
        for (player in 1..move[1].code) {
            players.add(GameModelPlayer(players.size - 1, dealer, true))
        }
        for (cpu in 1..move[2].code) {
            players.add(GameModelPlayer(players.size - 1, dealer, false))
        }
        rules.nextRound()
    }

    fun getListener(): GameModelListener {
        return listener
    }
}