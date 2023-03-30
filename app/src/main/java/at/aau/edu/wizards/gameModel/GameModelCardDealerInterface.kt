package at.aau.edu.wizards.gameModel

interface GameModelCardDealerInterface {

    /**
     * Deals a random card, that is still left in the current set of cards.
     * (Was not already dealt)
     */
    fun dealCardInSet(playerId: Int, parent: GameModel): GameModelCard

    /**
     * Resets the set of cards, to a fresh set of cards.
     * Meaning: all cards can be dealt again.
     */
    fun resetSet()
}