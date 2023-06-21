package at.aau.edu.wizards.gameModel

import at.aau.edu.wizards.R
import at.aau.edu.wizards.ui.gameboard.GameBoardHeader
import at.aau.edu.wizards.ui.gameboard.GameBoardViewModel
import kotlinx.coroutines.delay

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
    private val board = ArrayList<GameModelCard>()
    var winningCard: GameModelCard = GameModelCard.NoCard
        private set
    var guessing = false
        private set
    val headerList = ArrayList<GameBoardHeader>()

    private var lastSet = 0
    var winningCardPopUp = WinningCardPopUp(GameModelCard.NoCard, 0, false)

    data class Card(val card: GameModelCard, val playerId: Int)
    data class Guess(val guess: Int, val playerId: Int)
    data class Score(val score: Int, val playerId: Int)
    data class WinningCardPopUp(
        val lastCardWon: GameModelCard,
        val lastPlayerWon: Int,
        val visible: Boolean
    )

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

    fun getIconOfPlayer(id: Int): Int {
        return players[id].icon
    }

    private fun getCurrentWins(id: Int): Int {
        return rules.getAmountWon(id)
    }

    suspend fun update() {
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
        calculateWinningCardPopUp()
        viewModel?.updateData(parent)
    }

    private fun calculateHeader() {
        if (headerList.isNotEmpty()) {
            for (player in 0 until numberOfPlayers) {
                headerList[player] = headerList[player].copy(
                    name = rules.players[player].name,
                    icon = rules.players[player].icon,
                    guess = getCurrentGuessOfPlayer(player),
                    wins = getCurrentWins(player),
                    score = getCurrentScoreOfPlayer(player)
                )
            }
        } else {
            for (player in 0 until numberOfPlayers) {
                headerList.add(
                    GameBoardHeader(
                        getIconOfPlayer(player),
                        getNameOfPlayer(player),
                        getCurrentGuessOfPlayer(player),
                        getCurrentWins(player),
                        getCurrentScoreOfPlayer(player)
                    )
                )
            }
        }
    }

    private suspend fun calculateWinningCardPopUp() {
        if (rules.numberOfSet > lastSet) {
            winningCardPopUp = WinningCardPopUp(rules.lastCardWon, rules.lastPlayerWon, true)
            lastSet = rules.numberOfSet
            popUpVisibilityDelay()
        }
    }

    private suspend fun popUpVisibilityDelay() {
        activePlayer = winningCardPopUp.lastPlayerWon
        viewModel?.updateData(parent)
        delay(3000)
        winningCardPopUp = WinningCardPopUp(
            winningCardPopUp.lastCardWon,
            winningCardPopUp.lastPlayerWon,
            false
        )
        activePlayer = rules.currentPlayer
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

    fun getNameOfPlayer(id: Int): String {
        for (player in players) {
            if (player.id == id) {
                return player.name
            }
        }
        return "MissingPlayer"
    }

    fun getIconFromId(icon: Int): Int {
        return when (icon) {
            1 -> R.drawable.icon1
            2 -> R.drawable.icon2
            3 -> R.drawable.icon3
            4 -> R.drawable.icon4
            5 -> R.drawable.icon5
            6 -> R.drawable.icon6
            7 -> R.drawable.icon7
            8 -> R.drawable.icon8
            9 -> R.drawable.icon9
            10 -> R.drawable.icon10
            11 -> R.drawable.icon11
            12 -> R.drawable.icon12
            13 -> R.drawable.icon13
            14 -> R.drawable.icon14
            15 -> R.drawable.icon15
            16 -> R.drawable.icon16
            17 -> R.drawable.icon17
            18 -> R.drawable.icon18
            else -> R.drawable.icon19
        }
    }

    fun cheaterCall(cheater: Int) {
        viewModel?.gameModel?.foundCheater(cheater)
    }

    fun hasCheated(): Boolean {
        for (player in players) {
            if (player.hasCheated) {
                return true
            }
        }
        return false
    }

}
