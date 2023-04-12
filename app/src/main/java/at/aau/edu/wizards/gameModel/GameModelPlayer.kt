package at.aau.edu.wizards.gameModel

import kotlin.math.abs

class GameModelPlayer(
    val id: Int,
    private val dealer: GameModelDealer,
    val isHuman: Boolean
) {

    val cards = ArrayList<GameModelCard>()
    val scores = ArrayList<Int>()
    val guesses = ArrayList<Int>()

    fun dealCards(amount: Int) {
        for (card in 1..amount) {
            cards.add(dealer.dealCardInSet())
        }
    }

    fun hasColor(color: GameModelCard.Color): Boolean {
        for (card in cards) {
            when (card) {
                is GameModelCard.Normal -> {
                    if (card.color == color) {
                        return true
                    }
                }
                else -> {
                    continue
                }
            }
        }
        return false
    }

    fun score(wins: Int) {
        if (guesses.isEmpty()) {
            throw Exception("Failed to score: No guess was set!")
        }
        val guess =
            guesses[guesses.lastIndex] //doing this via a extra variable because it gives us more code coverage, prob an error in jacoco
        if (guess == wins) {
            scores.add(20 + (wins * 10))
        } else {
            scores.add((wins * 10) - ((abs(guesses[guesses.size - 1] - wins)) * 10))
        }
    }
}