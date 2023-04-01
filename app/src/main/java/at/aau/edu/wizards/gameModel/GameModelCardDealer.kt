package at.aau.edu.wizards.gameModel

import kotlin.random.Random

class GameModelCardDealer : GameModelCardDealerInterface {
    private val set = ArrayList<String>()
    private lateinit var cardHash: String

    override fun dealCardInSet(playerId: Int, parent: GameModel): GameModelResult<GameModelCard> {
        if (set.size >= 60) {
            return GameModelResult.Failure(Exception("Failed to create card in set: Trying to create more than 60 cards!"))
        } else if (playerId >= parent.listOfPlayers.size) {
            return GameModelResult.Failure(Exception("Failed to create card in set: Player does not exist!"))
        }
        cardHash = buildString {
            append(Random.nextInt(0, 14).toChar())
            append(Random.nextInt(1, 4).toChar())
            append(playerId)
        }
        getNearest()
        set.add(cardHash)
        return GameModelResult.Success(GameModelCard(cardHash, parent))
    }

    /**
     * Gets the nearest card of the set not already dealt.
     * Used to decrease overhead of random guesses.
     */
    private fun getNearest() {
        while (set.contains(cardHash)) {
            if (cardHash[0].code == 0 && cardHash[1].code > 1) {
                cardHash = buildString {
                    append(14.toChar())
                    append((cardHash[1].code - 1).toChar())
                    append(cardHash[2])
                }
            } else if (cardHash[0].code == 0 && cardHash[1].code == 1) {
                cardHash = buildString {
                    append(14.toChar())
                    append(4.toChar())
                    append(cardHash[2])
                }
            } else {
                cardHash = buildString {
                    append((cardHash[0].code - 1).toChar())
                    append(cardHash[1])
                    append(cardHash[2])
                }
            }
        }
    }

    override fun resetSet() {
        set.clear()
    }

}