package at.aau.edu.wizards.api.model

import at.aau.edu.wizards.model.GameModelCard

sealed class GameModelPlayer {
    private val cards = ArrayList<GameModelCard>() //current hand
    private var guess: Int? = null
    private var won: Int = 0
    private val score = ArrayList<Int>()

    fun addCard(card: GameModelCard) {
        cards.add(card)
    }

    fun removeCard(card: GameModelCard) {
        if (cards.contains(card)) {
            cards.remove(card)
            return
        }
        throw Exception("Failed to remove: Card does not exist!")
    }

    fun getCards(): ArrayList<GameModelCard> {
        return cards
    }

    fun getAmountWon(): Int {
        return won
    }

    fun getGuessWon(): Int {
        if (guess == null) {
            throw Exception("Failed to get Guess Amount: Guess was not set - Illegal to call between plays!")
        }
        return guess!!
    }

    fun setGuessWon(guess: Int) {
        this.guess = guess
    }

    fun hasGuess(): Boolean {
        if (guess == null) {
            return false
        }
        return true
    }

    fun score() {
        if (guess == null) {
            throw Exception("Failed to score: Guess was not set - Illegal to call between plays!")
        }
        if (guess!! == won) {
            score.add(20 + (won * 10))
        } else {
            score.add((won * 10) - ((guess!! - won) * 10))
        }
        guess = null
        won = 0
    }

    class GameModelPlayerHuman : GameModelPlayer() {

    }

    class GameModelPlayerCPU : GameModelPlayer() {

    }
}
