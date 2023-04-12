package at.aau.edu.wizards.gameModel

class GameModel {

    private val players = ArrayList<GameModelPlayer>()
    private var dealer = GameModelDealer(0)
    private var rules = GameModelRules(players, 0, dealer, this)
    var listener = GameModelListener(rules, players)
        private set

    fun sendMessage(move: String): Boolean {
        if ((legalMessageGuess(move) && (move[0].code - 60) / 11 == rules.id) || (legalMessageCard(
                move
            ) && rules.localPlayerOwns(dealer.getCardFromHash(move[0].code))) || move == "EndGame"
        ) {
            //TODO send to network
            return true
        }
        return false
    }

    fun receiveMessage(move: String): Boolean {
        return if (players.isEmpty()) { //Check if game still needs to be initialized, if initialize it / exit
            if (legalMessageInit(move)) {
                init(move)
                listener.update()
                true
            } else {
                false
            }
        } else if (legalMessageCard(move)) { //Check if player is playing a card
            rules.playCard(dealer.getCardFromHash(move[0].code))
            listener.update()
            true
        } else if (legalMessageGuess(move)) {
            players[(move[0].code - 60) / 11].guesses.add((move[0].code - 60) % 11)
            listener.update()
            true
        } else {
            false
        }
    }

    private fun legalMessageInit(move: String): Boolean {
        return move.length > 3
                && move[0].code < move[1].code + move[2].code
                && move[1].code + move[2].code in 3..6
                && legalIntInStringFormat(move.substring(3, move.length))
    }

    private fun legalIntInStringFormat(int: String): Boolean {
        return try {
            int.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun legalMessageCard(move: String): Boolean {
        if (move.length == 1
            && move[0].code < 60
            && rules.currentPlayerOwns(
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
        if (move.length == 1 && move[0].code in 60..(59 + players.size * 11)) {
            return true
        }
        return false
    }

    private fun init(move: String) {
        dealer = GameModelDealer(move.substring(3, move.length).toInt())
        rules = GameModelRules(players, move[0].code, dealer, this)
        listener = GameModelListener(rules, players)
        for (player in 1..move[1].code) {
            players.add(GameModelPlayer(players.size, dealer, true))
        }
        for (cpu in 1..move[2].code) {
            players.add(GameModelPlayer(players.size, dealer, false))
        }
        rules.init()
    }

}