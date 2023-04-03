package at.aau.edu.wizards.gameModel

import kotlin.random.Random

class GameModelCardDealer : GameModelCardDealerInterface {
    private val set = ArrayList<Int>()

    override fun dealCardInSet(playerId: Int): GameModelCard {
        if (set.size >= 60) {
            return GameModelCard(buildString {
                append(0.toChar())
                append(0.toChar())
                append(0.toChar())
            })
        }
        var card = Random.nextInt(0, 59)
        while (set.contains(card)) {
            card--
            if (card < 0) {
                card = 59
            }
        }
        set.add(card)
        return GameModelCard(buildString {
            append((card % 15).toChar())
            append((card / 15 + 1).toChar())
            append(playerId.toChar())
        })
    }

    override fun resetSet() {
        set.clear()
    }

}