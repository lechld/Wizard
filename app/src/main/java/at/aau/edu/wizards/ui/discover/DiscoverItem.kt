package at.aau.edu.wizards.ui.discover

import at.aau.edu.wizards.api.model.ClientConnection

sealed interface DiscoverItem {
    data class Header(val text: String) : DiscoverItem
    data class Requested(val connection: ClientConnection.Requested) : DiscoverItem
    data class Pending(val connection: ClientConnection.Found) : DiscoverItem
    data class Approved(val connection: ClientConnection.Connected) : DiscoverItem
}