package at.aau.edu.wizards.gameModel

interface GameModelCardInterface {
    val value: Int
    val color: Int
    val id: Int

    /**
     * Returns a string representation of the card used for network communication.
     */
    fun getString(): String

    /**
     * Tells you if the consists is of legal values.
     */
    fun isLegal(): Boolean

}