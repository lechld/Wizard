package at.aau.edu.wizards.ui.lobby

import android.provider.Settings.Global.getString
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AlertDialogLayout
import androidx.lifecycle.*
import at.aau.edu.wizards.*
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.gameModel.START_COMMAND
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    var numplayer = 1
    fun clicked(clickedItem: LobbyItem) {

        if (numplayer < 6) {
            when (clickedItem) {
                is LobbyItem.AddCpu -> {
                    addCpuPlayer()
                    numplayer = numplayer + 1
                }
                is LobbyItem.Requested -> {
                    accept(clickedItem)
                    numplayer = numplayer + 1
                }
                else -> {
                    // do nothing
                }
            }
        }else{
            println("Too many Player!")
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

    fun startGame(): Int {
        val connections = server.getConnections()
            .filterIsInstance(ServerConnection.Connected::class.java)

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