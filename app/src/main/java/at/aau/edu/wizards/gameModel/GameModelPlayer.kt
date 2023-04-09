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
                        return false
                    }
                }
                else -> {
                    continue
                }
            }
        }
        return true
    }

    fun score(wins: Int) {
        if (guesses.isEmpty()) {
            throw Exception("Failed to score: No guess was set!")
        }
        if (guesses[guesses.size - 1] == wins) {
            scores.add(20 + (wins * 10))
        } else {
            scores.add((wins * 10) - ((abs(guesses[guesses.size - 1] - wins)) * 10))
        }
    }
}