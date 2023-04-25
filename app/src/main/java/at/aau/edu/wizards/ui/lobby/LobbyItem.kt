package at.aau.edu.wizards.ui.lobby

import at.aau.edu.wizards.api.model.ServerConnection

sealed interface LobbyItem {
    data class Header(val text: String) : LobbyItem
    data class Requested(val connection: ServerConnection.ClientRequest) : LobbyItem
    data class Accepted(val connection: ServerConnection.Connected) : LobbyItem
}