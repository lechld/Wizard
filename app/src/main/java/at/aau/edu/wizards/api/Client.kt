package at.aau.edu.wizards.api

import kotlinx.coroutines.flow.Flow

interface Client {
    val connections: Flow<List<Connection>>
    fun getConnections(): List<Connection>

    fun startDiscovery()
    fun stopDiscovery()

    fun connect(connection: Connection)

    sealed interface Connection {
        val endpointId: String
        val endpointName: String

        data class Found(
            override val endpointId: String,
            override val endpointName: String,
        ) : Connection

        data class Requested(
            override val endpointId: String,
            override val endpointName: String,
        ) : Connection

        data class Connected(
            override val endpointId: String,
            override val endpointName: String,
        ) : Connection

        data class Failure(
            override val endpointId: String,
            override val endpointName: String,
        ) : Connection
    }
}