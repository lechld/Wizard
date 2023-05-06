package at.aau.edu.wizards.gameModel

import at.aau.edu.wizards.R
import at.aau.edu.wizards.ui.gameboard.GameBoardTheme

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

    fun getString(): String {
        when (this) {
            is Jester -> {
                return when (this.color) {
                    Color.Blue -> {
                        0.toChar().toString()
                    }
                    Color.Green -> {
                        15.toChar().toString()
                    }
                    Color.Orange -> {
                        30.toChar().toString()
                    }
                    Color.Red -> {
                        45.toChar().toString()
                    }
                }
            }
            NoCard -> {
                throw Exception("Failed to convert card to string: NoCard has no string representation")
            }
            is Normal -> {
                return when (this.color) {
                    Color.Blue -> {
                        (0 + this.value).toChar().toString()
                    }
                    Color.Green -> {
                        (15 + this.value).toChar().toString()
                    }
                    Color.Orange -> {
                        (30 + this.value).toChar().toString()
                    }
                    Color.Red -> {
                        (45 + this.value).toChar().toString()
                    }
                }
            }
            is Wizard -> {
                return when (this.color) {
                    Color.Blue -> {
                        14.toChar().toString()
                    }
                    Color.Green -> {
                        29.toChar().toString()
                    }
                    Color.Orange -> {
                        44.toChar().toString()
                    }
                    Color.Red -> {
                        59.toChar().toString()
                    }
                }
            }
        }
    }

    fun image(): Int {
        return when (this) {
            is Normal -> {
                imageNormal()
            }
            is Jester -> {
                when (this.color) {
                    Color.Blue -> R.drawable.bj
                    Color.Green -> R.drawable.gj
                    Color.Orange -> R.drawable.oj
                    Color.Red -> R.drawable.rj
                }
            }
            is Wizard -> {
                when (this.color) {
                    Color.Blue -> R.drawable.bw
                    Color.Green -> R.drawable.gw
                    Color.Orange -> R.drawable.ow
                    Color.Red -> R.drawable.rw
                }
            }
            is NoCard -> {
                R.drawable.nocard
            }
        }
    }

    private fun imageNormal(): Int {
        return when ((this as Normal).color) {
            Color.Blue -> {
                when (this.value) {
                    1 -> R.drawable.b1
                    2 -> R.drawable.b2
                    3 -> R.drawable.b3
                    4 -> R.drawable.b4
                    5 -> R.drawable.b5
                    6 -> R.drawable.b6
                    7 -> R.drawable.b7
                    8 -> R.drawable.b8
                    9 -> R.drawable.b9
                    10 -> R.drawable.b10
                    11 -> R.drawable.b11
                    12 -> R.drawable.b12
                    else -> R.drawable.b13
                }
            }
            Color.Green -> {
                when (this.value) {
                    1 -> R.drawable.g1
                    2 -> R.drawable.g2
                    3 -> R.drawable.g3
                    4 -> R.drawable.g4
                    5 -> R.drawable.g5
                    6 -> R.drawable.g6
                    7 -> R.drawable.g7
                    8 -> R.drawable.g8
                    9 -> R.drawable.g9
                    10 -> R.drawable.g10
                    11 -> R.drawable.g11
                    12 -> R.drawable.g12
                    else -> R.drawable.g13
                }
            }
            Color.Orange -> {
                when (this.value) {
                    1 -> R.drawable.o1
                    2 -> R.drawable.o2
                    3 -> R.drawable.o3
                    4 -> R.drawable.o4
                    5 -> R.drawable.o5
                    6 -> R.drawable.o6
                    7 -> R.drawable.o7
                    8 -> R.drawable.o8
                    9 -> R.drawable.o9
                    10 -> R.drawable.o10
                    11 -> R.drawable.o11
                    12 -> R.drawable.o12
                    else -> R.drawable.o13
                }
            }
            Color.Red -> {
                when (this.value) {
                    1 -> R.drawable.r1
                    2 -> R.drawable.r2
                    3 -> R.drawable.r3
                    4 -> R.drawable.r4
                    5 -> R.drawable.r5
                    6 -> R.drawable.r6
                    7 -> R.drawable.r7
                    8 -> R.drawable.r8
                    9 -> R.drawable.r9
                    10 -> R.drawable.r10
                    11 -> R.drawable.r11
                    12 -> R.drawable.r12
                    else -> R.drawable.r13
                }
            }
        }
    }

    fun getGameBoardTheme(): GameBoardTheme {
        return when (this) {
            is Normal -> {
                when (this.color) {
                    Color.Blue -> GameBoardTheme.Blue
                    Color.Green -> GameBoardTheme.Green
                    Color.Orange -> GameBoardTheme.Orange
                    Color.Red -> GameBoardTheme.Red
                }
            }
            is Wizard -> {
                when (this.color) {
                    Color.Blue -> GameBoardTheme.Blue
                    Color.Green -> GameBoardTheme.Green
                    Color.Orange -> GameBoardTheme.Orange
                    Color.Red -> GameBoardTheme.Red
                }
            }
            else -> GameBoardTheme.No
        }
    }

    fun getNumber(): String {
        return when(this){
            is Jester -> "J"
            is Normal -> this.value.toString()
            is Wizard -> "W"
            is NoCard -> ""
        }
    }
}