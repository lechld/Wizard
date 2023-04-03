package at.aau.edu.wizards.gameModel

interface GameModelCardDealerInterface {

    /**
     * Deals a random card, that is still left in the current set of cards.
     * (Was not already dealt)
     * Returns GameModelCard with all values 0 for going over card limit. (Empty card)
     */
    fun dealCardInSet(playerId: Int): GameModelCard

    /**
     * Resets the set of cards, to a fresh set of cards.
     * Meaning: all cards can be dealt again.
     */
    fun resetSet()
}