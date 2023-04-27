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
                    mutableCards.value = model.listener.getHandOfPlayer(model.localPlayer())
                    mutableBoard.value = model.listener.board
                }
            }
        }

        val seed = Random.nextInt().toString()
        var iteration = 0
        model.receiveMessage(buildString {
            append(iteration++.toChar())
            append(connections.size.toChar())
            append(amountCpu.toChar())
            append(seed)
        })
        connections.forEach {
            server.send(it, buildString {
                append(iteration++.toChar())
                append(connections.size.toChar())
                append(amountCpu.toChar())
                append(seed)
            })
        }
        mutableCards.value = model.listener.getHandOfPlayer(model.localPlayer())
        mutableBoard.value = model.listener.board
    }

    override fun sendMessage(move: String) {
        if(model.receiveMessage(move)) {
            viewModelScope.launch {
                server.getConnections().filterIsInstance(ServerConnection.Connected::class.java)
                    .forEach {
                        server.send(it, move)
                    }
            }
            mutableCards.value = model.listener.getHandOfPlayer(model.localPlayer())
            mutableBoard.value = model.listener.board
        }
    }
}