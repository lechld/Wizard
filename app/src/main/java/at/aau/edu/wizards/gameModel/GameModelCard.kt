package at.aau.edu.wizards.gameModel

class GameModelCard(card: String) : GameModelCardInterface {

    override val value = card[0].code
    override val color = card[1].code
    override val id = card[2].code

    override fun getString(): String {
        return buildString {
            append(value.toChar())
            append(color.toChar())
            append(id.toChar())
        }
    }

    override fun isLegal(): Boolean {
        if (value < 15 && color in 1..4) {
            return true
        }
        return false
    }
}