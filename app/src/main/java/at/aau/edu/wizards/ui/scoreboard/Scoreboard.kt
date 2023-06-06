package at.aau.edu.wizards.ui.scoreboard

data class Scoreboard(
    val playerIcon: Int,
    val playerName: String,
    val score: Int,
    val playerGuess: Int
)