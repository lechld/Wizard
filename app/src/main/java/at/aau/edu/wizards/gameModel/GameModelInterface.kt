package at.aau.edu.wizards.gameModel

interface GameModelInterface {

    val listOfPlayers: ArrayList<GameModelPlayer>

    /**
     * Checks if a move is legal and then sends it to the server.
     * Returns a [GameModelResult] depending on if it was successful or not.
     * Hint: Does not in any means change the board, this is done via [receiveMove].
     */
    fun sendMove(move: String): GameModelResult<Unit>

    /**
     * Receives a move from the server, and updates the internal Board depending on the received move.
     * Returns a [GameModelResult] depending on if it was successful or not.
     */
    fun receiveMove(move: String): GameModelResult<Unit>

    /**
     * Checks if the received message does indeed represent a legal card value
     */
    fun legalMessageCard(hash: String, numberOfPlayer: Int): Boolean
}