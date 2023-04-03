package at.aau.edu.wizards.gameModel

class GameModelDataListener(private val parent: GameModel, private val rules: GameModelRules) :
    GameModelDataListenerInterface {
    private var activePlayer = 0
    private var numberOfPlayers = 0
    private var currentHands = ArrayList<GameModelCard>()
    private var activeTrump = GameModelCard(buildString {
        append(0.toChar())
        append(0.toChar())
        append(0.toChar())
    })
    private var currentGuess = ArrayList<Guess>()
    private var totalScores = ArrayList<Score>()

    data class Guess(val guess: Int, val playerId: Int)
    data class Score(val score: Int, val playerId: Int)

    override fun getActivePlayer(): Int {
        return activePlayer
    }

    override fun getTotalNumberOfPlayers(): Int {
        return numberOfPlayers
    }

    override fun getCurrentHandOfPlayer(id: Int): ArrayList<GameModelCard> {
        val list = ArrayList<GameModelCard>()
        for (card in currentHands) {
            if (card.id == id) {
                list.add(card)
            }
        }
        return list
    }

    override fun getActiveTrump(): GameModelCard {
        return activeTrump
    }

    override fun getCurrentGuessOfPlayer(id: Int): Int {
        for (guess in currentGuess) {
            if (guess.playerId == id) {
                return guess.guess
            }
        }
        return 0
    }

    override fun getAllScoresOfPlayer(id: Int): ArrayList<Int> {
        val list = ArrayList<Int>()
        for (score in totalScores) {
            if (score.playerId == id) {
                list.add(score.score)
            }
        }
        return list
    }

    override fun getCurrentScoreOfPlayer(id: Int): Int {
        var total = 0
        for (score in totalScores.size - 1 downTo 0) {
            if (totalScores[score].playerId == id) {
                total += totalScores[score].score
            }
        }
        return total
    }

    override fun update() {
        activePlayer = rules.getCurrentPlayer()
        numberOfPlayers = parent.listOfPlayers.size
        currentHands.clear()
        for (player in parent.listOfPlayers) {
            for (card in player.getCurrentCards()) {
                currentHands.add(card)
            }
        }
        activeTrump = rules.getActiveTrump()
        currentGuess.clear()
        for (player in parent.listOfPlayers) {
            currentGuess.add(Guess(player.getCurrentGuess(), player.id))
        }
        totalScores.clear()
        for (player in parent.listOfPlayers) {
            for (score in player.getScores()) {
                totalScores.add(Score(score, player.id))
            }
        }
    }
}