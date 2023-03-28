package at.aau.edu.wizards.api.model

import at.aau.edu.wizards.model.GameModelCard

class GameModelBoard(private val parent: GameModelInstance) {
    private var trump: Int = 0
    private var winningCard: GameModelCard? = null
    private val inPlay = ArrayList<GameModelCard>()
    private val cheated = ArrayList<GameModelPlayer>()

    private fun setTrump(trump: GameModelCard) {
        if (!trump.checkLegal()) {
            throw Exception("Unable to set trump: Illegal Card Value")
        } else if (trump.getValue() == 0) {
            this.trump = 0
        } else if (trump.getValue() == 14) {
            //TODO call player choose trump function
        } else {
            this.trump = trump.getColor()
        }
    }

    fun getTrump(): Int {
        return trump
    }

    fun addCard(card: GameModelCard) {
        if (inPlay.isEmpty()) {
            setTrump(card)
            winningCard = card
        } else if (winningCard!!.getValue() == 14 || card.getValue() == 0) {
        } else if (card.getValue() == 14 || winningCard!!.getValue() == 0) {
            winningCard = card
        } else if (card.getColor() == winningCard!!.getColor()) {
            //same color - no need for further checks
            if (card.getValue() > winningCard!!.getValue()) {
                winningCard = card
            }
        } else if (card.getColor() == trump) {
            if (!isLegalPlay(card)) {
                throw Exception("Could not play card: Illegal Move - Use addCardIllegal to play instead!");
            }
            winningCard = card
        } else {
            if (!isLegalPlay(card)) {
                throw Exception("Could not play card: Illegal Move - Use addCardIllegal to play instead!");
            }
        }
        inPlay.add(card)
        parent.cardPlayed()
    }

    fun addCardIllegal(card: GameModelCard) {
        if (isLegalPlay(card)) {
            throw Exception("Could not play card: Legal Move - Use addCard to play instead!");
        } else if (card.getColor() == trump) {
            winningCard = card
        }
        cheated.add(card.getOwner())
        inPlay.add(card)
        parent.cardPlayed()
    }

    fun hasCheated(player: GameModelPlayer): Boolean {
        if (cheated.contains(player)) {
            return true
        }
        return false
    }

    fun isLegalPlay(card: GameModelCard): Boolean {
        for (hand in card.getOwner().getCards()) {
            if (hand.getColor() == winningCard!!.getColor()) {
                return false
            }
        }
        return true
    }

    fun cardsInPlay(): Int {
        return inPlay.size
    }

    fun getWinningCard(): GameModelCard {
        if (winningCard == null) {
            throw Exception("Could not return winning card: Winning card is null - Illegal to call outside of play!")
        }
        return winningCard!!
    }

    fun getWinningPlayer(): GameModelPlayer {
        if (winningCard == null) {
            throw Exception("Could not return winning card: Winning card is null - Illegal to call outside of play!")
        }
        return winningCard!!.getOwner()
    }

    fun clearBoardRound() {
        trump = 0
        winningCard = null
        inPlay.clear()
    }

    fun clearBoardTurn() {
        clearBoardRound()
        cheated.clear()
    }
}