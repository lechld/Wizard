package at.aau.edu.wizards.gameModel

import at.aau.edu.wizards.ui.gameboard.GameBoardHeader
import at.aau.edu.wizards.ui.gameboard.GameBoardViewModel

class GameModelListener(
    private val rules: GameModelRules,
    private val players: ArrayList<GameModelPlayer>,
    private val viewModel: GameBoardViewModel?,
    private val parent: GameModel
) {
    var activePlayer = 0
        private set
    var numberOfPlayers = 0
        private set
    private val hands = ArrayList<Card>()
    var trump: GameModelCard = GameModelCard.NoCard
        private set
    private val guesses = ArrayList<Guess>()
    private val scores = ArrayList<Score>()
    val board = ArrayList<GameModelCard>()
    var winningCard: GameModelCard = GameModelCard.NoCard
        private set
    var guessing = false
    val headerList = ArrayList<GameBoardHeader>()

    data class Card(val card: GameModelCard, val playerId: Int)
    data class Guess(val guess: Int, val playerId: Int)
    data class Score(val score: Int, val playerId: Int)

    fun getHandOfPlayer(id: Int): ArrayList<GameModelCard> { //potential to internally store hands and only update when player makes a turn, would reduce cpu overhead slightly
        val hand = ArrayList<GameModelCard>()
        for (card in hands) {
            if (card.playerId == id) {
                hand.add(card.card)
            }
        }
        return hand
    }

    fun getCurrentGuessOfPlayer(id: Int): Int {
        for (i in 1..guesses.size) {
            if (guesses[guesses.size - i].playerId == id) {
                return guesses[guesses.size - i].guess
            }
        }
        return 0
    }

    fun getAllScoresOfPlayer(id: Int): ArrayList<Int> {
        val returnScore = ArrayList<Int>()
        for (score in scores) {
            if (score.playerId == id) {
                returnScore.add(score.score)
            }
        }
        return returnScore
    }

    fun getCurrentScoreOfPlayer(id: Int): Int {
        var returnScore = 0
        for (score in scores) {
            if (score.playerId == id) {
                returnScore += score.score
            }
        }
        return returnScore
    }

    private fun getIconOfPlayer(id: Int): Int {
        return if (id in 0 until numberOfPlayers) {
            players[id].icon
        } else {
            1
        }
    }

    private fun getNameOfPlayer(id: Int): String {
        return buildString {
            append("Player")
            append(id.toString())
        }
    }

    private fun getCurrentWins(id: Int): Int {
        return rules.getAmountWon(id)
    }

    fun update() {
        activePlayer = rules.currentPlayer
        numberOfPlayers = players.size
        trump = rules.trump
        winningCard = rules.winningCard
        guessing = rules.wantsGuess
        calculateBoard()
        hands.clear()
        guesses.clear()
        scores.clear()
        for (player in players) {
            for (card in player.cards) {
                hands.add(Card(card, player.id))
            }
            for (guess in player.guesses) {
                guesses.add(Guess(guess, player.id))
            }
            for (score in player.scores) {
                scores.add(Score(score, player.id))
            }
        }
        calculateHeader()
        viewModel?.updateData(parent)
    }

    fun localPlayer(): Int {
        return rules.id
    }

    private fun calculateHeader() {
        if (headerList.isNotEmpty()) {
            for (player in 0 until numberOfPlayers) {
                headerList[player] = headerList[player].copy(
                    guess = getCurrentGuessOfPlayer(player),
                    wins = getCurrentWins(player),
                    score = getCurrentScoreOfPlayer(player),
                    theme = trump.getGameBoardTheme()
                )
            }
        } else {
            for (player in 0 until numberOfPlayers) {
                headerList.add(
                    GameBoardHeader(
                        player,
                        getIconOfPlayer(player),
                        getNameOfPlayer(player),
                        getCurrentGuessOfPlayer(player),
                        getCurrentWins(player),
                        getCurrentScoreOfPlayer(player),
                        trump.getGameBoardTheme()
                    )
                )
            }
        }
    }

    private fun calculateBoard() {
        this.board.clear()
        for (card in rules.board) {
            this.board.add(card)
        }
        this.board.add(winningCard)
    }

    fun boardAsNewArray(): ArrayList<GameModelCard> {
        val returnBoard = ArrayList<GameModelCard>()
        for (card in board) {
            returnBoard.add(card)
        }
        return returnBoard
    }

    fun getRound(): Int {
        return rules.round
    }
}