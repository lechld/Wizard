package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.gameModel.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class ServerGameBoardViewModel(
    private val server: Server,
    amountCpu: Int,
) : GameBoardViewModel() {
    override val gameModel = GameModel(this)

    init {
        val connections =
            server.getConnectionsSync().filterIsInstance(ServerConnection.Connected::class.java)

        viewModelScope.launch {
            server.messages.collect { message ->
                if (message.value == MESSAGE1 || message.value == MESSAGE2 || message.value == MESSAGE3) {
                    if (winningText.value == null) {
                        _winningText.postValue(
                            WinningText(
                                message.value, 1
                            )
                        )
                    } else {
                        _winningText.postValue(
                            WinningText(
                                message.value,
                                winningText.value!!.iteration + 1
                            )
                        )
                    }
                    connections.forEach {
                        server.send(it, message.value)
                    }
                } else if (gameModel.receiveMessage(message.value)) {
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
        when (move) {
            END_COMMAND -> {
                _scoreboard.value = true
            }
            else -> {
                viewModelScope.launch {
                    if (move == MESSAGE1 || move == MESSAGE2 || move == MESSAGE3) {
                        if (winningText.value == null) {
                            _winningText.postValue(
                                WinningText(
                                    move, 1
                                )
                            )
                        } else {
                            _winningText.postValue(
                                WinningText(
                                    move,
                                    winningText.value!!.iteration + 1
                                )
                            )
                        }
                        server.getConnectionsSync()
                            .filterIsInstance(ServerConnection.Connected::class.java)
                            .forEach {
                                server.send(it, move)
                            }
                    } else if (gameModel.receiveMessage(move)) {
                        server.getConnectionsSync()
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
}