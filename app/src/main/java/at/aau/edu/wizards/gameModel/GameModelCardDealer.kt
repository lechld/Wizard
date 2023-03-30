package at.aau.edu.wizards.gameModel

import kotlin.random.Random

class GameModelCardDealer : GameModelCardDealerInterface {
    private val set = ArrayList<String>()
    private lateinit var cardHash: String

    override fun dealCardInSet(playerId: Int, parent: GameModel): GameModelCard {
        do {
            cardHash = buildString {
                append(Random.nextInt(0, 14).toChar())
                append(Random.nextInt(1, 4).toChar())
                append(playerId)
            }
        } while (set.contains(cardHash))
        set.add(cardHash)
        return GameModelCard(cardHash, parent)
    }

    override fun resetSet() {
        set.clear()
    }

}