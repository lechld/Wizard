package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.model.ClientConnection
import at.aau.edu.wizards.gameModel.END_COMMAND
import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.SHARE_NAMES_AND_AVATAR
import kotlinx.coroutines.launch

class ClientGameBoardViewModel(
    val client: Client,
    private val userinfo: String
) : GameBoardViewModel() {
    override val gameModel = GameModel(this)

    init {
        viewModelScope.launch {
            client.messages.collect { message ->
                if (message.value == SHARE_NAMES_AND_AVATAR) {
                    sendMessage(
                        240.toChar().toString() + userinfo + gameModel.localPlayer().toChar()
                            .toString()
                    )
                } else if (gameModel.receiveMessage(message.value)) {
                    updateData(gameModel)
                }
            }
        }
    }

    override fun sendMessage(move: String) {

        if (move == END_COMMAND) {
            _scoreboard.value = true
        } else {
            viewModelScope.launch {
                client.connectionsSync().filterIsInstance(ClientConnection.Connected::class.java)
                    .forEach {
                        client.send(it, move)
                    }
            }
        }
    }
}