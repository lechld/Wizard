package at.aau.edu.wizards.ui.lobby

import androidx.lifecycle.*
import at.aau.edu.wizards.*
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LobbyViewModel(
    private val server: Server,
    private val lobbyItemFactory: LobbyItemFactory = LobbyItemFactory()
) : ViewModel() {

    private val cpuPlayers = MutableStateFlow(0)

    private val connections = server.connections

    val items: LiveData<List<LobbyItem>> =
        connections.combine(cpuPlayers) { connections, cpuPlayers ->
            lobbyItemFactory.create(connections, cpuPlayers)
        }.asLiveData()

    fun startAdvertising() {
        server.startBroadcasting()
    }

    fun stopAdvertising() {
        server.stopBroadcasting()
    }

    fun clicked(clickedItem: LobbyItem) {
        when (clickedItem) {
            is LobbyItem.AddCpu -> {
                addCpuPlayer()
            }
            is LobbyItem.Requested -> {
                accept(clickedItem)
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun accept(lobbyItem: LobbyItem.Requested) {
        server.acceptClientRequest(lobbyItem.connection)
    }

    private fun decline(lobbyItem: LobbyItem.Requested) {
        server.declineClientRequest(lobbyItem.connection)
    }

    private fun addCpuPlayer() {
        cpuPlayers.tryEmit(cpuPlayers.value + 1)
    }

    fun startGame() {
        val connections = server.getConnections()
            .filterIsInstance(ServerConnection.Connected::class.java)

        //TODO: define the communication protocol

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