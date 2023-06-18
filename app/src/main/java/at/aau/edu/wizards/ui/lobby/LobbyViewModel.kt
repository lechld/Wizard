package at.aau.edu.wizards.ui.lobby

import androidx.lifecycle.*
import at.aau.edu.wizards.*
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.gameModel.START_COMMAND
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LobbyViewModel(
    private val server: Server, private val lobbyItemFactory: LobbyItemFactory = LobbyItemFactory()
) : ViewModel() {

    companion object {
        val MAX_PLAYERS = 6

        val randomCpuPlayerAvatars = IntArray(MAX_PLAYERS)

        init {
            for (i in 0..MAX_PLAYERS - 1) {
                randomCpuPlayerAvatars[i] = MainViewModel.avatarsList.random()
            }
        }
    }

    private val cpuPlayers = MutableStateFlow(0)

    private val connections = server.connections

    val items: Flow<List<LobbyItem>> =
        connections.combine(cpuPlayers) { connections, cpuPlayers ->
            lobbyItemFactory.create(connections, cpuPlayers)
        }

    fun startAdvertising() {
        server.startBroadcasting()
    }

    fun stopAdvertising() {
        server.stopBroadcasting()
    }

    var numPlayer: Int = 1
        private set

    var checkTooManyPlayer = false
        private set

    fun clicked(clickedItem: LobbyItem) {

        if (numPlayer < MAX_PLAYERS) {
            when (clickedItem) {
                is LobbyItem.AddCpu -> {
                    addCpuPlayer()
                    numPlayer += 1
                }
                is LobbyItem.Requested -> {
                    accept(clickedItem)
                    numPlayer += 1
                }
                else -> {
                    // do nothing
                    println("do nothing")
                }
            }
        } else {
            checkTooManyPlayer = true
        }

        if (numPlayer <= MAX_PLAYERS) {
            when (clickedItem) {
                is LobbyItem.RemoveCpu -> {
                    checkTooManyPlayer = false
                    removeCpuPlayer()
                    numPlayer -= 1
                }
                else -> {
                    // do nothing
                    println("do nothing")
                }
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

    private fun removeCpuPlayer() {
        cpuPlayers.tryEmit(cpuPlayers.value - 1)
    }

    fun startGame(): Int {
        val connections =
            server.getConnectionsSync().filterIsInstance(ServerConnection.Connected::class.java)

        connections.forEach { connection ->
            server.send(connection, START_COMMAND)
        }

        return cpuPlayers.value
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