package at.aau.edu.wizards.ui.discover

import at.aau.edu.wizards.api.model.ClientConnection

sealed interface MainscreenItem {
    data class Header(val text: String) : MainscreenItem
    data class Requested(val connection: ClientConnection.Requested) : MainscreenItem
    data class Pending(val connection: ClientConnection.Found) : MainscreenItem
    data class Approved(val connection: ClientConnection.Connected) : MainscreenItem
}