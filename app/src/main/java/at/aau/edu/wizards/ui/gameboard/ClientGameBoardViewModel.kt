package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.model.ClientConnection
import at.aau.edu.wizards.gameModel.*
import kotlinx.coroutines.launch

class ClientGameBoardViewModel(
    val client: Client,
) : GameBoardViewModel() {
    override val gameModel = GameModel(this)

    init {
        viewModelScope.launch {
            client.messages.collect { message ->
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
                } else if (gameModel.receiveMessage(message.value)) {
                    updateData(gameModel)
                }
            }
        }
    }

    override fun sendMessage(move: String) {
        when (move) {
            END_COMMAND -> {
                _scoreboard.value = true
            }
            else -> {
                viewModelScope.launch {
                    client.connectionsSync()
                        .filterIsInstance(ClientConnection.Connected::class.java)
                        .forEach {
                            client.send(it, move)
                        }
                }
            }
        }
    }
}