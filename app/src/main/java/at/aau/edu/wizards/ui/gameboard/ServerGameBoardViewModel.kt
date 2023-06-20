package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.gameModel.END_COMMAND
import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.SHARE_NAMES_AND_AVATAR
import kotlinx.coroutines.launch
import kotlin.random.Random

class ServerGameBoardViewModel(
    private val server: Server,
    amountCpu: Int,
    private val userinfo: String
) : GameBoardViewModel() {
    override val gameModel = GameModel(this)

    init {
        val connections =
            server.getConnectionsSync().filterIsInstance(ServerConnection.Connected::class.java)

        viewModelScope.launch {
            server.messages.collect { message ->
                connections.forEach {
                    server.send(it, message.value)
                }
                if (gameModel.receiveMessage(message.value)) {
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
            sendMessage(SHARE_NAMES_AND_AVATAR)
        }
    }

    override fun sendMessage(move: String) {
        when (move) {
            END_COMMAND -> {
                _scoreboard.value = true
            }
            else -> {
                viewModelScope.launch {
                    server.getConnectionsSync()
                        .filterIsInstance(ServerConnection.Connected::class.java)
                        .forEach {
                            server.send(it, move)
                        }
                    if (move == SHARE_NAMES_AND_AVATAR) {
                        sendMessage(
                            240.toChar().toString() + userinfo + gameModel.localPlayer().toChar()
                                .toString()
                        )
                    } else if (gameModel.receiveMessage(move)) {
                        updateData(gameModel)
                    }
                }
            }
        }
    }
}