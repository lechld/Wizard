package at.aau.edu.wizards.gameModel

class GameModelDebugger {

    fun forDebug(debugMessage: String): String {
        if (debugMessage.length == 1) {
            if (debugMessage[0].code < 60) {
                return buildString {
                    append("A CARD was played of type ")
                    if (debugMessage[0].code % 15 == 0) {
                        append("JESTER ")
                        append(getColor(debugMessage[0].code))
                    } else if (debugMessage[0].code % 15 == 14) {
                        append("WIZARD ")
                        append(getColor(debugMessage[0].code))
                    } else {
                        append("NORMAL ")
                        append(getColor(debugMessage[0].code))
                        append("of VALUE ")
                        append((debugMessage[0].code % 15).toString())
                    }
                }
            } else {
                return buildString {
                    append("A GUESS was played of VALUE: ")
                    append((debugMessage[0].code - 60) % 11)
                    append(" and of PLAYER: ")
                    append((debugMessage[0].code - 60) / 11)
                }
            }
        } else if (debugMessage == START_COMMAND || debugMessage == END_COMMAND) {
            return debugMessage
        } else if (debugMessage.length > 3) {
            return buildString {
                append("This is a game INIT(ializing) message! \n Player ID =")
                append(debugMessage[0].code.toString())
                append("\n Amount of HUMAN players: ")
                append(debugMessage[1].code.toString())
                append("\n Amount of CPU players: ")
                append(debugMessage[2].code.toString())
                append("\n SEED:")
                append(debugMessage.subSequence(3, debugMessage.length))
            }
        } else {
            return buildString {
                append("ERROR - This should not have happened, message is NOT a LEGAL play: \"")
                append(debugMessage)
                append("\"")
            }
        }
    }

    private fun getColor(card: Int): String {
        return when (card / 15) {
            0 -> "BLUE "
            1 -> "GREEN "
            2 -> "ORANGE "
            else -> "RED "
        }
    }
}