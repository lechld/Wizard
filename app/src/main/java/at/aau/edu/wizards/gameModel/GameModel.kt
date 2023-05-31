package at.aau.edu.wizards.gameModel

import at.aau.edu.wizards.ui.gameboard.GameBoardViewModel
import kotlin.random.Random

class GameModel(private val viewModel: GameBoardViewModel?) {

    private val players = ArrayList<GameModelPlayer>()
    private var dealer = GameModelDealer(0)
    private var rules = GameModelRules(players, 0, dealer, this, 0)
    var listener = GameModelListener(rules, players, viewModel, this)
        private set

    fun sendMessage(move: String): Boolean {
        return if (legalMessageGuessSend(move) || legalMessageCardSend(move) || move == END_COMMAND) {
            viewModel?.sendMessage(move)
            true
        } else {
            false
        }
    }

    private fun legalMessageGuessSend(move: String): Boolean {
        return (legalMessageGuess(move) && (move[0].code - 60) / 11 == rules.id)
    }

    private fun legalMessageCardSend(move: String): Boolean {
        return legalMessageCard(move) && legalPlayFromLocalPlayer(move) && rules.everyoneHasGuessed()
    }

    private fun legalPlayFromLocalPlayer(move: String): Boolean {
        return rules.localPlayerOwns(dealer.getCardFromHash(move[0].code)) && (rules.winningCard !is GameModelCard.Normal || playerPlaysCorrectColor(
            move
        ))
    }

    private fun playerPlaysCorrectColor(move: String): Boolean {
        return ((rules.winningCard as GameModelCard.Normal).color == getColor(move) || !players[rules.currentPlayer].hasColor(
            (rules.winningCard as GameModelCard.Normal).color
        ))
    }

    suspend fun receiveMessage(move: String): Boolean {
        return when (players.isEmpty()) {
            true -> {
                if (legalMessageInit(move)) {
                    init(move)
                    listener.update()
                    true
                } else {
                    false
                }
            }
            else -> {
                when (legalMessageCard(move) && rules.everyoneHasGuessed()) {
                    true -> {
                        rules.playCard(dealer.getCardFromHash(move[0].code))
                        listener.update()
                        true
                    }
                    else -> {
                        if (legalMessageGuess(move)) {
                            rules.addGuess(move[0].code)
                            listener.update()
                            true
                        } else {
                            false
                        }
                    }
                }
            }
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
        rules = GameModelRules(
            players,
            move[0].code,
            dealer,
            this,
            move.substring(3, move.length).toInt()
        )
        listener = GameModelListener(rules, players, viewModel, this)
        //TEMPORARY PLEASE REMOVE WHEN YOU IMPLEMENTED PROPER NAMING
        val list = listOf(
            "Pengu",
            "Angel",
            "Adolf",
            "Marley",
            "Smoker",
            "Crack",
            "Bamboo",
            "Tasty",
            "Staly",
            "Mickey",
            "Goofy",
            "Pink",
            "City",
            "Papaya",
            "Mango",
            "Jack",
            "Donald",
            "YouSuck",
            "GetAGrip",
            "Green Tea"
        )
        for (player in 1..move[1].code) {
            players.add(
                GameModelPlayer(
                    players.size,
                    dealer,
                    true,
                    Random.nextInt(1, 20),
                    list[Random.nextInt(list.size)]
                )
            )
        }
        for (cpu in 1..move[2].code) {
            players.add(
                GameModelPlayer(
                    players.size,
                    dealer,
                    false,
                    Random.nextInt(1, 20),
                    list[Random.nextInt(list.size)]
                )
            )
        }
        rules.init()
    }

    private fun getColor(hash: String): GameModelCard.Color {
        return when (hash[0].code / 15) {
            0 -> GameModelCard.Color.Blue
            1 -> GameModelCard.Color.Green
            2 -> GameModelCard.Color.Orange
            else -> GameModelCard.Color.Red
        }
    }

    fun localPlayer(): Int {
        return rules.id
    }

    fun sendGuessOfLocalPlayer(guess: Int): Boolean {
        return sendMessage(buildString { append((60 + rules.id * 11 + guess).toChar().toString()) })
    }

    fun updateGuessCount() {

        val newGuessList = listener.sendGuessList()
        //println(newGuessList[localPlayer()])
        //newGuessList[localPlayer()]

        //newGuessList[localPlayer()] = GameModelListener.Guess(listener.getCurrentGuessOfPlayer(localPlayer()), localPlayer())

        listener.updatedGuess(newGuessList)
    }
}