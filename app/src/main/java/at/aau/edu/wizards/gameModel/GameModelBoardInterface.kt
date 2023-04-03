package at.aau.edu.wizards.gameModel

interface GameModelBoardInterface {

    /**
     * Adds a trump (in string format) to the stack.
     * Failure if the string represents an illegal card.
     * For a no-trump round use "00"
     */
    fun addTrumpToStack(trump: String): GameModelResult<Unit>

    /**
     * Returns the winning card of the current board.
     * Failure if there is no winning card set.
     */
    fun getWinningCard(): GameModelResult<GameModelCard>

    /**
     * Returns the color code of the current Trump card.
     * Failure if there is no trump set. (This should not happen.)
     */
    fun getTrump(): GameModelResult<Int>

    /**
     * Sets the next trump saved in the board.
     * Failure if called more often than trump cards saved in the board.
     */
    fun nextTrump(): GameModelResult<Unit>

    /**
     * Adds a new card to the current game. It is saved as the winning card.
     * Failure if this is not a legal Card value.
     */
    fun addWinningCard(card: GameModelCard): GameModelResult<Unit>

    /**
     * Adds a new card to the current game. It is NOT saved as the winning card.
     * Failure if this is not a legal Card value.
     */
    fun addNonWinningCard(card: GameModelCard): GameModelResult<Unit>

    /**
     * Starts a new game.
     */
    fun clearGame()

    /**
     * Starts a new turn.
     */
    fun clearRound()

    /**
     * Returns the amount of games a player has won. This should be called before a new turn is started.
     */
    fun gamesWon(playerId: Int): Int

    /**
     * Same function as get Trump, but does not throw an error if it is null. (Instead returns 0)
     * Use this if you know trump cant be null.
     */
    fun getTrumpCantBeNull(): Int
}