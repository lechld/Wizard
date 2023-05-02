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
                    13 -> R.drawable.b13
                    20 -> R.drawable.b0
                    21 -> R.drawable.numberb1
                    22 -> R.drawable.numberb2
                    23 -> R.drawable.numberb3
                    24 -> R.drawable.numberb4
                    25 -> R.drawable.numberb5
                    26 -> R.drawable.numberb6
                    27 -> R.drawable.numberb7
                    28 -> R.drawable.numberb8
                    29 -> R.drawable.numberb9
                    else -> R.drawable.numberb10
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
                    13 -> R.drawable.g13
                    20 -> R.drawable.g0
                    21 -> R.drawable.numberg1
                    22 -> R.drawable.numberg2
                    23 -> R.drawable.numberg3
                    24 -> R.drawable.numberg4
                    25 -> R.drawable.numberg5
                    26 -> R.drawable.numberg6
                    27 -> R.drawable.numberg7
                    28 -> R.drawable.numberg8
                    29 -> R.drawable.numberg9
                    else -> R.drawable.numberg10
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
                    13 -> R.drawable.o13
                    20 -> R.drawable.o0
                    21 -> R.drawable.numbero1
                    22 -> R.drawable.numbero2
                    23 -> R.drawable.numbero3
                    24 -> R.drawable.numbero4
                    25 -> R.drawable.numbero5
                    26 -> R.drawable.numbero6
                    27 -> R.drawable.numbero7
                    28 -> R.drawable.numbero8
                    29 -> R.drawable.numbero9
                    else -> R.drawable.numbero10
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
                    13 -> R.drawable.r13
                    20 -> R.drawable.r0
                    21 -> R.drawable.numberr1
                    22 -> R.drawable.numberr2
                    23 -> R.drawable.numberr3
                    24 -> R.drawable.numberr4
                    25 -> R.drawable.numberr5
                    26 -> R.drawable.numberr6
                    27 -> R.drawable.numberr7
                    28 -> R.drawable.numberr8
                    29 -> R.drawable.numberr9
                    else -> R.drawable.numberr10
                }
            }
        }
    }

    fun imageBackground(): Int {
        return when (this) {
            is Normal -> {
                when (this.color) {
                    Color.Blue -> R.drawable.bb
                    Color.Green -> R.drawable.gb
                    Color.Orange -> R.drawable.ob
                    Color.Red -> R.drawable.rb
                }
            }
            is Wizard -> {
                when (this.color) {
                    Color.Blue -> R.drawable.bb
                    Color.Green -> R.drawable.gb
                    Color.Orange -> R.drawable.ob
                    Color.Red -> R.drawable.rb
                }
            }
            else -> R.drawable.nb
        }
    }

    fun imageSlice(): Int {
        return when (this) {
            is Normal -> {
                when (this.color) {
                    Color.Blue -> R.drawable.bslice
                    Color.Green -> R.drawable.gslice
                    Color.Orange -> R.drawable.oslice
                    Color.Red -> R.drawable.rslice
                }
            }
            is Wizard -> {
                when (this.color) {
                    Color.Blue -> R.drawable.bslice
                    Color.Green -> R.drawable.gslice
                    Color.Orange -> R.drawable.oslice
                    Color.Red -> R.drawable.rslice
                }
            }
            else -> R.drawable.nslice
        }
    }

    fun imageHeaderBackground(): Int {
        return when (this) {
            is Normal -> {
                when (this.color) {
                    Color.Blue -> R.drawable.bhb
                    Color.Green -> R.drawable.ghb
                    Color.Orange -> R.drawable.ohb
                    Color.Red -> R.drawable.rhb
                }
            }
            is Wizard -> {
                when (this.color) {
                    Color.Blue -> R.drawable.bhb
                    Color.Green -> R.drawable.ghb
                    Color.Orange -> R.drawable.ohb
                    Color.Red -> R.drawable.rhb
                }
            }
            else -> R.drawable.nhb
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
}