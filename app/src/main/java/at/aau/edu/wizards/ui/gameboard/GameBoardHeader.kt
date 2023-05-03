package at.aau.edu.wizards.ui.gameboard

data class GameBoardHeader(
    val player: Int,
    val icon: Int,
    val name: String,
    var guess: Int,
    var wins: Int,
    var score: Int,
    var theme: GameBoardTheme
)
