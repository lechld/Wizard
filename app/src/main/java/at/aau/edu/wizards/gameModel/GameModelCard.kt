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

}