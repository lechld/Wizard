package at.aau.edu.wizards.gameModel

interface GameModelPlayerInterface {
    val id: Int
    val cards: ArrayList<GameModelCard>

    /**
     * Returns a string representation of the player used for network communication.
     */
    fun getString(): String


}