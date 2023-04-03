package at.aau.edu.wizards.gameModel

import kotlin.math.abs

class GameModelPlayer(val id: Int, val isCPU: Int) : GameModelPlayerInterface {

    private val cards = ArrayList<GameModelCard>()
    private val currentCards = ArrayList<GameModelCard>()
    private var guess: Int = 0
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
        if (turn < 1) {
            return GameModelResult.Failure(Exception("Failed to deal hand: Trying to go under the turn minimum of 1!"))
        } else if (turn > 10) {
            return GameModelResult.Failure(Exception("Failed to deal hand: Trying to go over the turn limit of 10!"))
        } else if (currentCards.isNotEmpty()) {
            return GameModelResult.Failure(Exception("Failed to deal hand: There are still cards in play!"))
        }
        var pos = 0
        for (skip in 1 until turn) {
            pos += skip
        }
        if (cards.size < pos + turn) {
            return GameModelResult.Failure(Exception("Failed to deal hand: Not enough cards left do deal or/and turn smaller or equal to 0!"))
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
            if (currentCards[card].color == color && currentCards[card].value in 1..13) {
                return true
            }
        }
        return false
    }

    override fun removeCardFromHand(card: GameModelCard): GameModelResult<Unit> {
        for (index in 0 until currentCards.size) {
            if (currentCards[index].getString() == card.getString()) {
                currentCards.removeAt(index)
                return GameModelResult.Success(Unit)
            }
        }
        return GameModelResult.Failure(Exception("Failed to remove card from hand: Card does not exist!"))
    }

    override fun cardsEmpty(): Boolean {
        if (currentCards.isEmpty()) {
            return true
        }
        return false
    }

    override fun getGuess() {
        //TODO implement and call GameModel sendMove - currently mock function
        setGuess(1)
    }

    override fun setGuess(receivedGuess: Int) {
        guess = receivedGuess
    }

    override fun score(amountWon: Int) {
        if (guess == amountWon) {
            scores.add(20 + (amountWon * 10))
        } else {
            scores.add((amountWon * 10) - ((abs(guess - amountWon)) * 10))
        }
        guess = 0
    }

    override fun addCardToPlayerStack(card: GameModelCard): GameModelResult<Unit> {
        if (!card.isLegal() || card.id != id) {
            return GameModelResult.Failure(Exception("Failed to add card to player stack: Card is not legal or does not belong to player!"))
        }
        cards.add(card)
        return GameModelResult.Success(Unit)
    }

    override fun getCurrentCards(): ArrayList<GameModelCard> {
        val list = ArrayList<GameModelCard>()
        for (card in currentCards) {
            list.add(GameModelCard(card.getString()))
        }
        return list
    }

    override fun getCurrentGuess(): Int {
        return guess
    }

    override fun getScores(): ArrayList<Int> {
        val list = ArrayList<Int>()
        for (score in scores) {
            list.add(score)
        }
        return list
    }
}