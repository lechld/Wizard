package at.aau.edu.wizards.ui.discover

import at.aau.edu.wizards.api.model.ClientConnection

class MainscreenItemFactory {

    fun create(
        connections: List<ClientConnection>
    ): List<MainscreenItem> {
        val found = connections.filterIsInstance(
            ClientConnection.Found::class.java
        )
        val requested = connections.filterIsInstance(
            ClientConnection.Requested::class.java
        )
        val connected = connections.filterIsInstance(
            ClientConnection.Connected::class.java
        )

        val result = mutableListOf<MainscreenItem>()

        if (found.isNotEmpty()) {
            result.add(MainscreenItem.Header("Open Games"))

            result.addAll(
                found.map { MainscreenItem.Pending(it) }
            )
        }

        if (requested.isNotEmpty()) {
            result.add(MainscreenItem.Header("Requested Games"))

            result.addAll(
                requested.map { MainscreenItem.Requested(it) }
            )
        }

        if (connected.isNotEmpty()) {
            result.add(MainscreenItem.Header("Approved. Waiting for Game to start"))

            result.addAll(
                connected.map { MainscreenItem.Approved(it) }
            )
        }

        if (found.isEmpty() && requested.isEmpty() && connected.isEmpty()) {
            result.add(MainscreenItem.Header("Currently searching for games to join"))
        }

        return result
    }
}