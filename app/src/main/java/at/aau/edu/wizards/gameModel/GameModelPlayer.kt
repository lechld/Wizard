package at.aau.edu.wizards.gameModel

import kotlin.math.abs

class GameModelPlayer(
    val id: Int,
    private val dealer: GameModelDealer,
    val isHuman: Boolean,
    val icon: Int,
    val name: String
) {

    val cards = ArrayList<GameModelCard>()
    val scores = ArrayList<Int>()
    val guesses = ArrayList<Int>()
    var hasCheated = false

    fun dealCards(amount: Int) {
        for (card in 1..amount) {
            cards.add(dealer.dealCardInSet())
        }
    }

    fun hasColor(color: GameModelCard.Color): Boolean {
        return cards.any { it is GameModelCard.Normal && it.color == color }
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