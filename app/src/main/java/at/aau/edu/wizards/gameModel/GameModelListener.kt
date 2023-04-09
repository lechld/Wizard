package at.aau.edu.wizards.gameModel

class GameModelListener(
    private val rules: GameModelRules,
    private val players: ArrayList<GameModelPlayer>
) {
    private var activePlayer = 0
    private var numberOfPlayers = 0
    private var hands = ArrayList<Card>()
    private var trump: GameModelCard = GameModelCard.NoCard
    private var guesses = ArrayList<Guess>()
    private var scores = ArrayList<Score>()

    data class Card(val card: GameModelCard, val playerId: Int)
    data class Guess(val guess: Int, val playerId: Int)
    data class Score(val score: Int, val playerId: Int)

    fun getActivePlayer(): Int {
        return activePlayer
    }

    fun getTotalNumberOfPlayers(): Int {
        return numberOfPlayers
    }

    fun getHandOfPlayer(id: Int): ArrayList<GameModelCard> {
        val hand = ArrayList<GameModelCard>()
        for (card in hands) {
            if (card.playerId == id) {
                hand.add(card.card)
            }
        }
        return hand
    }

    fun getTrump(): GameModelCard {
        return trump
    }

    fun getCurrentGuessOfPlayer(id: Int): Int {
        for (i in guesses.size - 1..0) {
            if (guesses[i].playerId == id) {
                return guesses[i].guess
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

    fun update() {
        activePlayer = rules.getActivePlayer()
        numberOfPlayers = players.size
        trump = rules.getTrump()
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