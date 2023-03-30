package at.aau.edu.wizards.gameModel

class GameModelBoard() : GameModelBoardInterface {

    private lateinit var trump: String
    val trumpStack = ArrayList<String>()
    private var winningCard: GameModelCard? = null
    private val game = ArrayList<GameModelCard>()
    private val winningCards = ArrayList<GameModelCard>()

    fun getWinningCard(): GameModelResult<GameModelCard> {
        if (winningCard == null) {
            return GameModelResult.Failure(Exception("Unable to return winning card: No winning card exists"))
        }
        return GameModelResult.Success<GameModelCard>(winningCard!!)
    }

    fun getTrump(): Int {
        //TODO append this for a getTrumpUI function
        return trump[0].code
    }

    fun nextTrump() {
        trump = trumpStack[0]
        trumpStack.removeAt(0)
    }

    fun addWinningCard(card: GameModelCard) {
        winningCard = card
        addNonWinningCard(card)
    }

    fun addNonWinningCard(card: GameModelCard) {
        game.add(card)
    }

    fun clearGame() {
        winningCard = null
        game.clear()
    }

    fun clearTurn() {
        clearGame()
        winningCards.clear()
    }

    fun gamesWon(playerId: Int): Int {
        var counter = 0
        for (card in winningCards) {
            if (card.owner.id == playerId) {
                counter++
            }
        }
        return counter
    }
}