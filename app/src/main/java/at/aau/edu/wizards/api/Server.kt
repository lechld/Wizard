package at.aau.edu.wizards.api

import kotlinx.coroutines.flow.Flow

interface Server {
    val connections: Flow<List<Connection>>
    fun getConnections(): List<Connection>

    fun startBroadcasting()
    fun stopBroadcasting()

    fun acceptClientRequest(connection: Connection)
    fun declineClientRequest(connection: Connection)

    sealed interface Connection {
        val endpointId: String
        val endpointName: String

        data class ClientRequest(
            override val endpointId: String,
            override val endpointName: String
        ) : Connection

        data class Connected(
            override val endpointId: String,
            override val endpointName: String
        ) : Connection

        data class Failure(
            override val endpointId: String,
            override val endpointName: String
        ) : Connection
    }
}

