package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.gameModel.END_COMMAND
import at.aau.edu.wizards.gameModel.GameModel
import kotlinx.coroutines.launch
import kotlin.random.Random

class ServerGameBoardViewModel(
    private val server: Server,
    amountCpu: Int,
) : GameBoardViewModel() {
    override val gameModel = GameModel(this)

    init {
        val connections =
            server.getConnections().filterIsInstance(ServerConnection.Connected::class.java)

        viewModelScope.launch {
            server.messages.collect { message ->
                if (gameModel.receiveMessage(message.value)) {
                    connections.forEach {
                        server.send(it, message.value)
                    }
                    updateData(gameModel)
                }
            }
        }

        viewModelScope.launch {
            val seed = Random.nextInt().toString()
            var iteration = 0
            gameModel.receiveMessage(buildString {
                append(iteration++.toChar())
                append((connections.size + 1).toChar())
                append(amountCpu.toChar())
                append(seed)
            })
            connections.forEach {
                server.send(it, buildString {
                    append(iteration++.toChar())
                    append((connections.size + 1).toChar())
                    append(amountCpu.toChar())
                    append(seed)
                })
            }
            updateData(gameModel)
        }
    }

    override fun sendMessage(move: String) {
        if (move == END_COMMAND) {
            _scoreboard.value = true
        } else {
            viewModelScope.launch {
                if (gameModel.receiveMessage(move)) {
                    server.getConnections()
                        .filterIsInstance(ServerConnection.Connected::class.java)
                        .forEach {
                            server.send(it, move)
                        }
                }
                updateData(gameModel)
            }
        }
    }
}