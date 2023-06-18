package at.aau.edu.wizards.ui.lobby

import at.aau.edu.wizards.MainViewModel
import at.aau.edu.wizards.USERDATA_AVATAR
import at.aau.edu.wizards.USERDATA_USERNAME
import at.aau.edu.wizards.USERDATA_UUID
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.ui.lobby.LobbyViewModel.Companion.MAX_PLAYERS
import at.aau.edu.wizards.ui.lobby.LobbyViewModel.Companion.randomCpuPlayerAvatars

class LobbyItemFactory {

    fun create(
        connections: List<ServerConnection>,
        cpuPlayers: Int,
    ): List<LobbyItem> {

        val clientRequest = connections.filterIsInstance(
            ServerConnection.ClientRequest::class.java
        )
        val connected = connections.filterIsInstance(
            ServerConnection.Connected::class.java
        )

        val result = mutableListOf<LobbyItem>()

        if (clientRequest.isNotEmpty()) {
            result.add(LobbyItem.Header("Player requests"))

            result.addAll(
                clientRequest.map { LobbyItem.Requested(it) }
            )
        }

        result.add(LobbyItem.Header("Joined players"))
        result.add(LobbyItem.Accepted(ServerConnection.Connected(
            USERDATA_UUID, USERDATA_USERNAME + ":" +
                USERDATA_AVATAR
        )))

        result.addAll(
            connected.map { LobbyItem.Accepted(it) }
        )

        if (connected.isNotEmpty()) {

        }

        if (clientRequest.isEmpty() && connected.isEmpty()) {
            result.add(LobbyItem.Header("Waiting for players to join"))
        }

        result.add(LobbyItem.Header(" "))
        result.add(LobbyItem.Header("CPU Players"))

        for (i in 1..cpuPlayers) {
            result.add(LobbyItem.CpuPlayer("CPU Player $i:${randomCpuPlayerAvatars[i-1]}"))
        }
        result.add(LobbyItem.AddCpu)

        if (cpuPlayers > 0)
        {
            result.add(LobbyItem.RemoveCpu)
        }

        return result
    }
}