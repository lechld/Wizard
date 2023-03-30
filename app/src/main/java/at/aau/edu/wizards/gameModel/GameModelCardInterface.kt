package at.aau.edu.wizards.gameModel

interface GameModelCardInterface {
    val value:Int
    val color:Int
    val owner:GameModelPlayer

    /**
     * Returns a string representation of the card used for network communication.
     */
    fun getString():String
}