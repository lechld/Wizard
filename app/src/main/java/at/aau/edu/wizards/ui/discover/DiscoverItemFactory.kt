package at.aau.edu.wizards.ui.discover

import at.aau.edu.wizards.api.model.ClientConnection

class DiscoverItemFactory {

    fun create(
        connections: List<ClientConnection>
    ): List<DiscoverItem> {
        val found = connections.filterIsInstance(
            ClientConnection.Found::class.java
        )
        val requested = connections.filterIsInstance(
            ClientConnection.Requested::class.java
        )
        val connected = connections.filterIsInstance(
            ClientConnection.Connected::class.java
        )

        val result = mutableListOf<DiscoverItem>()

        if (found.isNotEmpty()) {
            result.add(DiscoverItem.Header("Open Games"))

            result.addAll(
                found.map { DiscoverItem.Pending(it) }
            )
        }

        if (requested.isNotEmpty()) {
            result.add(DiscoverItem.Header("Requested Games"))

            result.addAll(
                requested.map { DiscoverItem.Requested(it) }
            )
        }

        if (connected.isNotEmpty()) {
            result.add(DiscoverItem.Header("Approved. Waiting for Game to start"))

            result.addAll(
                connected.map { DiscoverItem.Approved(it) }
            )
        }

        if (found.isEmpty() && requested.isEmpty() && connected.isEmpty()) {
            result.add(DiscoverItem.Header("Currently searching for games to join"))
        }

        return result
    }
}