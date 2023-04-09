package at.aau.edu.wizards.gameModel

sealed class GameModelCard {

    data class Normal(
        val color: Color,
        val value: Int
    ) : GameModelCard()

    data class Jester(
        val color: Color
    ) : GameModelCard()

    data class Wizard(
        val color: Color
    ) : GameModelCard()

    object NoCard : GameModelCard()

    sealed class Color {
        object Blue : Color()
        object Green : Color()
        object Orange : Color()
        object Red : Color()
    }

}