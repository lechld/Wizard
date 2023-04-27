package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.gameModel.GameModel
import kotlinx.coroutines.launch

class ClientGameBoardViewModel(
    val client: Client,
) : GameBoardViewModel() {
    val model = GameModel(this)

    init {
        viewModelScope.launch {
            client.messages.collect { message ->
                if(model.receiveMessage(message.value)){
                    mutableCards.value = model.listener.getHandOfPlayer(model.localPlayer())
                }
            }
        }
    }

    override fun sendMessage(move: String) {
        viewModelScope.launch {
            client.getConnections().filterIsInstance(ServerConnection.Connected::class.java)
                .forEach {
                    client.send(it, move)
                }
        }
    }
}