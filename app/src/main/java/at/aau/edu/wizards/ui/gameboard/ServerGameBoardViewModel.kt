package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.gameModel.GameModel
import kotlinx.coroutines.launch
import kotlin.random.Random

class ServerGameBoardViewModel(
    val server: Server,
    amountCpu: Int,
) : GameBoardViewModel() {
    val model = GameModel(this)

    init {
        val connections =
            server.getConnections().filterIsInstance(ServerConnection.Connected::class.java)

        viewModelScope.launch {
            server.messages.collect { message ->
                if (model.receiveMessage(message.value)) {
                    connections.forEach {
                        server.send(it, message.value)
                    }
                }
            }
        }

        var iteration = 1
        connections.forEach {
            server.send(it, buildString {
                append(iteration++.toChar())
                append(connections.size.toChar())
                append(amountCpu.toChar())
                append(Random.nextInt().toString())
            })
        }
    }

    override fun sendMessage(move: String) {
        model.receiveMessage(move)
        viewModelScope.launch {
            server.getConnections().filterIsInstance(ServerConnection.Connected::class.java)
                .forEach {
                    server.send(it, move)
                }
        }
    }
}