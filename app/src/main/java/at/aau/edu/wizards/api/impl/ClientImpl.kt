package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.BuildConfig
import at.aau.edu.wizards.api.Client
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class ClientImpl(
    private val connectionsClient: ConnectionsClient,
    private val payloadCallback: PayloadCallback,
    private val userIdentifier: String = GENERATED_NAME,
    private val applicationIdentifier: String = BuildConfig.APPLICATION_ID,
) : Client {

    private val _connections = MutableStateFlow(emptyList<Client.Connection>())
    override val connections: Flow<List<Client.Connection>> = _connections

    override fun getConnections(): List<Client.Connection> {
        return _connections.value
    }

    override fun startDiscovery() {
        val connections = mutableListOf<Client.Connection>()

        connectionsClient.startDiscovery(
            applicationIdentifier,
            object : EndpointDiscoveryCallback() {
                override fun onEndpointFound(
                    endpointId: String,
                    endpointInfo: DiscoveredEndpointInfo
                ) {
                    connections.add(Client.Connection.Found(endpointId, endpointInfo.endpointName))

                    _connections.tryEmit(connections)
                }

                override fun onEndpointLost(endpointId: String) {
                    connections.removeAll { it.endpointId == endpointId }

                    _connections.tryEmit(connections)
                }

            },
            discoveryOptions
        )
    }

    override fun stopDiscovery() {
        connectionsClient.stopDiscovery()
    }

    override fun connect(connection: Client.Connection) {
        // As long we are automatically calling acceptConnection below
        // it's fine to update connections immediately
        // Move to Requested state, server needs to approve connection too
        val immediateUpdate = getConnections().toMutableList()
        immediateUpdate.removeAll { it.endpointId == connection.endpointId }
        immediateUpdate.add(Client.Connection.Requested(connection.endpointId, connection.endpointName))
        _connections.tryEmit(immediateUpdate)

        connectionsClient.requestConnection(
            userIdentifier,
            connection.endpointId,
            object : ConnectionLifecycleCallback() {
                override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
                    // we selected to connect, therefore we can directly accept that connection
                    connectionsClient.acceptConnection(endpointId, payloadCallback)
                }

                override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                    val connections = getConnections().toMutableList()
                    val old = connections.first { it.endpointId == endpointId }
                    val new = if (result.status.isSuccess) {
                        Client.Connection.Connected(endpointId, old.endpointName)
                    } else {
                        Client.Connection.Failure(endpointId, old.endpointName)
                    }

                    connections.remove(old)
                    connections.add(new)

                    _connections.tryEmit(connections)
                }

                override fun onDisconnected(endpointId: String) {
                    val connections = _connections.value.toMutableList()
                    val old = connections.first { it.endpointId == endpointId }
                    val new = Client.Connection.Failure(endpointId, old.endpointName)

                    connections.remove(old)
                    connections.add(new)

                    _connections.tryEmit(connections)
                }
            })
    }
}
