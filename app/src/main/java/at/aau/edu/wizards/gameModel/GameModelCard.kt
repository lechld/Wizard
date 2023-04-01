package at.aau.edu.wizards.gameModel

class GameModelCard(card: String, parent: GameModel) : GameModelCardInterface {

    override val value = card[0].code
    override val color = card[1].code
    override val owner = parent.listOfPlayers[card[2].code] //Note - this will lead to an error if initialized with illegal values

    override fun getString(): String {
        return buildString {
            append(value.toChar())
            append(color.toChar())
            append(owner.id.toChar())
        }
    }

    override fun isLegal(): Boolean {
        if (value in 0..14 && color in 1..4) {
            return true
        }
        return false
    }
}