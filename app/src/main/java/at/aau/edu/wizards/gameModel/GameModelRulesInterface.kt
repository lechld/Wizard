package at.aau.edu.wizards.gameModel

interface GameModelRulesInterface {
    /**
     * Checks if a move is legal using standard wizard rules.
     * This is only meant for the local player performing the move.
     */
    fun checkMoveLegal(move: String): Boolean

    /**
     * Checks if a move is legal, disregarding the rule that you have to play the same color if you own it.
     * This is only meant for the local player performing the move.
     */
    fun checkMoveLegalCheat(move: String): Boolean

    /**
     * Checks if it is a players active turn.
     */
    fun isActivePlayer(id: Int): Boolean

    /**
     * Initializes the first round of the game.
     * Multiple reasons for failure, see function for further information.
     */
    fun initFirstRound(): GameModelResult<Unit>

    /**
     * Plays a given card. If it is not possible trough legal means, it will assume player is cheating.
     * Failure if card is illegal or does not exist.
     */
    fun playCard(move: String): GameModelResult<Unit>

    /**
     * Adds a trump card to the trump stack.
     * Failure if card is of an illegal value for trump.
     */
    fun addTrump(trump: String): GameModelResult<Unit>

    /**
     * Returns the id of the active player. (Player who can play cards.)
     */
    fun getCurrentPlayer(): Int

    /**
     * Returns the active trump card. 0 0 0 is returned for no trump!
     */
    fun getActiveTrump(): GameModelCard
}