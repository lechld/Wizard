package at.aau.edu.wizards.gameModel

class GameModelPlayer(val id: Int, val isCPU: Int) : GameModelPlayerInterface {

    val cards = ArrayList<GameModelCard>()
    private val currentCards = ArrayList<GameModelCard>()
    private var guess: Int? = null
    private val scores = ArrayList<Int>()

    override fun getString(): String {
        return buildString {
            append(15.toChar())
            append(isCPU.toChar())
            append(id.toChar())
            for (card in 0 until cards.size) {
                append(cards[card].getString())
            }
        }
    }

    override fun dealHand(turn: Int): GameModelResult<Unit> {
        if (turn > 10) {
            return GameModelResult.Failure(Exception("Failed to deal hand: Trying to go over the turn limit of 10!"))
        } else if (currentCards.isNotEmpty()) {
            return GameModelResult.Failure(Exception("Failed to deal hand: There are still cards in play!"))
        }
        var pos = 0
        for (skip in 1..turn) {
            pos += skip
        }
        if (cards.size <= pos + turn) {
            return GameModelResult.Failure(Exception("Failed to deal hand: Not enough cards left do deal!"))
        }
        for (card in 0 until turn) {
            currentCards.add(cards[pos + card])
        }
        pos += turn
        return GameModelResult.Success(Unit)
    }


    override fun cardsContain(hash: String): Boolean {
        for (card in 0 until currentCards.size) {
            if (currentCards[card].getString() == hash) {
                return true
            }
        }
        return false
    }

    override fun cardsContainColor(color: Int): Boolean {
        for (card in 0 until currentCards.size) {
            if (currentCards[card].color == color) {
                return true
            }
        }
        return false
    }

    override fun removeCardFromHand(card: GameModelCard): GameModelResult<Unit> {
        if (!currentCards.contains(card)) {
            return GameModelResult.Failure(Exception("Failed to remove card from hand: Card does not exist!"))
        }
        for (index in 0 until currentCards.size) {
            if (currentCards[index] == card) {
                currentCards.removeAt(index)
            }
        }
        return GameModelResult.Success(Unit)
    }

    override fun cardsEmpty(): Boolean {
        if (currentCards.isEmpty()) {
            return true
        }
        return false
    }

    override fun getGuess(): GameModelResult<Unit> {
        //TODO implement and call GameModel sendMove - currently mock function
        setGuess(1)
        return GameModelResult.Success(Unit)
    }

    override fun setGuess(receivedGuess: Int) {
        guess = receivedGuess
    }

    override fun score(amountWon: Int): GameModelResult<Unit> {
        if (guess == null) {
            return GameModelResult.Failure(Exception("Failed to score: Guess was not set"))
        }
        if (guess!! == amountWon) {
            scores.add(20 + (amountWon * 10))
        } else {
            scores.add((amountWon * 10) - ((guess!! - amountWon) * 10))
        }
        guess = null
        return GameModelResult.Success(Unit)
    }
}