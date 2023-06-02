package at.aau.edu.wizards.ui.gameboard

data class GameBoardHeader(
    val player: Int,
    val icon: Int,
    val name: String,
    val guess: Int,
    val wins: Int,
    val score: Int
)
