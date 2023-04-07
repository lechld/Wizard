package at.aau.edu.wizards.api

import at.aau.edu.wizards.api.model.Connection
import kotlinx.coroutines.flow.Flow

interface Server {
    val connections: Flow<List<Connection>>
    fun getConnections(): List<Connection>

    fun startBroadcasting()
    fun stopBroadcasting()

    fun acceptPendingConnection(endpointId: String)
    fun declinePendingConnection(endpointId: String)
}

