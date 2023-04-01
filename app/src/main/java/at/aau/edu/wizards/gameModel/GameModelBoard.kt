package at.aau.edu.wizards.gameModel

class GameModelBoard : GameModelBoardInterface {

    private var trump: String? = null
    private var winningCard: GameModelCard? = null
    private val game = ArrayList<GameModelCard>()
    private val winningCards = ArrayList<GameModelCard>()
    private val trumpStack = ArrayList<String>()

    override fun addTrumpToStack(trump: String): GameModelResult<Unit> {
        if (trump.length != 2 || trump[0].code !in 0..14 || trump[1].code !in 0..5) {
            return GameModelResult.Failure(Exception("Unable to add trump to stack: Illegal trump card!"))
        }
        trumpStack.add(trump)
        return GameModelResult.Success(Unit)
    }

    override fun getWinningCard(): GameModelResult<GameModelCard> {
        if (winningCard == null) {
            return GameModelResult.Failure(Exception("Unable to return winning card: No winning card exists!"))
        }
        return GameModelResult.Success(winningCard!!)
    }


    override fun getTrump(): GameModelResult<Int> {
        if (trump == null) {
            return GameModelResult.Failure(Exception("Unable to return trump color: Trump not set!"))
        }
        return GameModelResult.Success(trump!![1].code)
    }


    override fun nextTrump(): GameModelResult<Unit> {
        if (trumpStack.isEmpty()) {
            return GameModelResult.Failure(Throwable("Failed to set next trump: Trying to set more trumps than defined in config!"))
        }
        trump = trumpStack[0]
        trumpStack.removeAt(0)
        return GameModelResult.Success(Unit)
    }


    override fun addWinningCard(card: GameModelCard): GameModelResult<Unit> {
        if (!card.isLegal()) {
            GameModelResult.Failure(Exception("Failed to add winning card to board: Illegal card!"))
        }
        winningCard = card
        addNonWinningCard(card)
        return GameModelResult.Success(Unit)
    }


    override fun addNonWinningCard(card: GameModelCard): GameModelResult<Unit> {
        if (!card.isLegal()) {
            return GameModelResult.Failure(Exception("Failed to add card to board: Illegal card!"))
        }
        game.add(card)
        return GameModelResult.Success(Unit)
    }


    override fun clearGame() {
        winningCard = null
        game.clear()
    }


    override fun clearRound() {
        clearGame()
        winningCards.clear()
    }


    override fun gamesWon(playerId: Int): Int {
        var counter = 0
        for (card in winningCards) {
            if (card.owner.id == playerId) {
                counter++
            }
        }
        return counter
    }
}