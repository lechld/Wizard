package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.BuildConfig
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.model.Connection
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ClientImpl(
    private val connectionsClient: ConnectionsClient,
    private val payloadCallback: PayloadCallback,
    private val userIdentifier: String = GENERATED_NAME,
    private val applicationIdentifier: String = BuildConfig.APPLICATION_ID,
) : Client {

    private val _connections = MutableStateFlow<List<Connection>>(emptyList())
    override val connections: StateFlow<List<Connection>> = _connections.asStateFlow()

    override fun getConnections(): List<Connection> {
        return _connections.value
    }

    override fun startDiscovery() {
        val connections = mutableListOf<Connection>()

        connectionsClient.startDiscovery(
            applicationIdentifier,
            object : EndpointDiscoveryCallback() {
                override fun onEndpointFound(
                    endpointId: String,
                    endpointInfo: DiscoveredEndpointInfo
                ) {
                    connections.add(Connection.Pending(endpointId))

                    _connections.tryEmit(connections)
                }

                override fun onEndpointLost(endpointId: String) {
                    connections.remove(Connection.Pending(endpointId))

                    _connections.tryEmit(connections)
                }

            },
            discoveryOptions
        )
    }

    override fun stopDiscovery() {
        connectionsClient.stopDiscovery()
    }

    override fun connect(endpointId: String) {
        connectionsClient.requestConnection(
            userIdentifier,
            endpointId,
            object : ConnectionLifecycleCallback() {
                override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
                    // we selected to connect, avoid pending and directly accept
                    connectionsClient.acceptConnection(endpointId, payloadCallback)

                    // info contains the userIdentifier as endpointName
                }

                override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                    val connections = _connections.value.toMutableList()

                    connections.remove(Connection.Pending(endpointId))

                    if (result.status.isSuccess) {
                        connections.add(Connection.Success(endpointId))
                    }

                    _connections.tryEmit(connections)
                }

                override fun onDisconnected(endpointId: String) {
                    val connections = _connections.value.toMutableList()
                    connections.removeAll { it.endpointId == endpointId }

                    _connections.tryEmit(connections)
                }
            })
    }
}
