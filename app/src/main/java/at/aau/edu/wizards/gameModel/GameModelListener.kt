package at.aau.edu.wizards.gameModel

class GameModelListener(
    private val rules: GameModelRules,
    private val players: ArrayList<GameModelPlayer>
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

    data class Card(val card: GameModelCard, val playerId: Int)
    data class Guess(val guess: Int, val playerId: Int)
    data class Score(val score: Int, val playerId: Int)

    fun getHandOfPlayer(id: Int): ArrayList<GameModelCard> {
        val hand = ArrayList<GameModelCard>()
        for (card in hands) {
            if (card.playerId == id) {
                hand.add(card.card)
            }
        }
        return hand
    }

    fun getBoard(): ArrayList<GameModelCard> {
        val cards = ArrayList<GameModelCard>()
        for (card in board) {
            cards.add(card)
        }
        return cards
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
        return if (id in 0 until numberOfPlayers) {
            players[id].icon
        } else {
            1
        }
    }

    fun getNameOfPlayer(id: Int): String {
        return buildString {
            append("Player")
            append(id.toString())
        }
    }

    fun getCurrentWins(id: Int): Int {
        return rules.getAmountWon(id)
    }

    fun update() {
        activePlayer = rules.currentPlayer
        numberOfPlayers = players.size
        trump = rules.trump
        winningCard = rules.winningCard
        board.clear()
        for (card in rules.board) {
            board.add(card)
        }
        board.add(rules.winningCard)
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
    }
}