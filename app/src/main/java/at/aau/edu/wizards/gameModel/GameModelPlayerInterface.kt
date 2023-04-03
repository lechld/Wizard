package at.aau.edu.wizards.gameModel

interface GameModelPlayerInterface {

    /**
     * Returns a string representation of the player used for network communication.
     */
    fun getString(): String

    /**
     * Deals a new set of cards depending on what was saved in the initialization of the game.
     * Multiple possible causes for failure, check function for further details.
     */
    fun dealHand(turn: Int): GameModelResult<Unit>

    /**
     * Checks if the current cards contain a certain card.
     */
    fun cardsContain(hash: String): Boolean

    /**
     * Checks if the current cards contain a certain color.
     */
    fun cardsContainColor(color: Int): Boolean

    /**
     * Remove a card from hand. (Usually when it is played.)
     * Failure if card does not exist.
     */
    fun removeCardFromHand(card: GameModelCard): GameModelResult<Unit>

    /**
     * Checks if player has no cards left to play.
     */
    fun cardsEmpty(): Boolean

    /**
     * Gets the guess for the current round from the player and sends it to the server.
     */
    fun getGuess()

    /**
     * Sets the guess for the current round.
     */
    fun setGuess(receivedGuess: Int)

    /**
     * Calculates the score of the current round and saves it.
     * Failure if guess was not set.
     */
    fun score(amountWon: Int)

    /**
     * Adds a card to the future draw pool of the player.
     * Failure if card is illegal or does not belong to the player.
     */
    fun addCardToPlayerStack(card: GameModelCard): GameModelResult<Unit>
}