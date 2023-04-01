package at.aau.edu.wizards.gameModel

interface GameModelConfigInterface {

    /**
     * Creates a setup for the game. This should only be called by the server.
     * Multiple causes for Failure, see function for more details.
     */
    fun createConfig(numberOfPlayerHuman: Int, numberOfPlayerCPU: Int): GameModelResult<Unit>

    /**
     * Returns the setup for a game. This should only be called by the server, exactly once for every player.
     * Failure if called more often than human players exist.
     */
    fun getConfig(): GameModelResult<String>

}