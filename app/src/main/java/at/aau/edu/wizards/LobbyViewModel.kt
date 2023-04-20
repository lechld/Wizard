package at.aau.edu.wizards.ui.lobby

import androidx.lifecycle.*
import at.aau.edu.wizards.*
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
//import at.aau.edu.wizards.ui.util.LiveEvent
import kotlinx.coroutines.flow.map

class LobbyViewModel(
    private val server: Server,
    private val lobbyItemFactory: LobbyItemFactory = LobbyItemFactory()
) : ViewModel() {

    val items: LiveData<List<LobbyItem>> = server.connections.map { connections ->
        lobbyItemFactory.create(connections)
    }.asLiveData()

    fun startAdvertising() {
        server.startBroadcasting()
    }

    fun stopAdvertising() {
        server.stopBroadcasting()
    }

    fun accept(lobbyItem: LobbyItem.Requested) {
        server.acceptClientRequest(lobbyItem.connection)
    }

    fun decline(lobbyItem: LobbyItem.Requested) {
        server.declineClientRequest(lobbyItem.connection)
    }

    fun startGame() {
        val connections = server.getConnections()
            .filterIsInstance(ServerConnection.Connected::class.java)

        connections.forEach {
            server.send(it, "START")
        }

    }

    class Factory(
        private val server: Server
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(LobbyViewModel::class.java)) {
                LobbyViewModel(server) as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}