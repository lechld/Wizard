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
     *
     * Legal Moves:
     * You can send a card, this represents you playing the card: It consists of a three letter string consisting of the value, color and player id. Value between 0 and 14, color between 1 and 3 and player between 0 and 2-5 (depending on amount of players).
     * You can send a guess, this is represented by a value starting at ten for guess 0 for player 0. It then goes up module 11 since you cant guess more than 10.
     * So starting at 21 you would guess 0 for player 1, 32 for player 2, etc. etc.
     * You can send a trump, this is represented by a normal card, but player id is 6. (Reserved for System cards.) Card 0 0 6 is also accepted, this means there is no trump.
     * Just 1 is used to update the data listener, this only used for internal updates after a new configuration for example and should not be called. (Though it wont result in any error, it is just useless.)
     */
    fun receiveMove(move: String): GameModelResult<Unit>

    /**
     * Checks if the received message does indeed represent a legal card value
     */
    fun legalMessageCard(hash: String, numberOfPlayer: Int): Boolean
}