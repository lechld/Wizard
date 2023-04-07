package at.aau.edu.wizards.api

import at.aau.edu.wizards.api.model.Connection
import kotlinx.coroutines.flow.Flow

interface Client {
    val connections: Flow<List<Connection>>
    fun getConnections(): List<Connection>

    fun startDiscovery()
    fun stopDiscovery()

    fun connect(endpointId: String)
}