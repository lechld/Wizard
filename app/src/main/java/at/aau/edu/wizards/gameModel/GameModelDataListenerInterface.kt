package at.aau.edu.wizards.gameModel

interface GameModelDataListenerInterface {

    /**
     * Gets the id of the active player. (Player who can currently play a card.)
     */
    fun getActivePlayer(): Int

    /**
     * Gets the total number of players in the game. (Including CPUs.)
     */
    fun getTotalNumberOfPlayers(): Int

    /**
     * Gets the current hand of a certain player.
     */
    fun getCurrentHandOfPlayer(id: Int): ArrayList<GameModelCard>

    /**
     * Gets the active Frump card. Note: 0 0 0 stands for no card. Legal Trumps always end with player id 6. (Reserved for system cards.)
     */
    fun getActiveTrump(): GameModelCard

    /**
     * Gets the current guess of a certain player.
     */
    fun getCurrentGuessOfPlayer(id: Int): Int

    /**
     * Gets the scores of all turns (up until the current turn) of a certain player.
     */
    fun getAllScoresOfPlayer(id: Int): ArrayList<Int>

    /**
     * Gets the total score of a certain player. You get the same result by adding all values of getAllScoresOfPlayer of a certain player.
     */
    fun getCurrentScoreOfPlayer(id: Int): Int

    /**
     * Refreshes the data in the listener. This should not be called by anything other than the [GameModel],
     * since it updates it automatically whenever there is a change in the board.
     */
    fun update()
}