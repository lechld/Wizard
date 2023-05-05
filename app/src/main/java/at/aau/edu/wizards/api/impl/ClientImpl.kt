package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.BuildConfig
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.model.ClientConnection
import at.aau.edu.wizards.api.MessageReceiver
import at.aau.edu.wizards.api.MessageSender
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class ClientImpl(
    private val connectionsClient: ConnectionsClient,
    private val userIdentifier: String = GENERATED_NAME,
    private val applicationIdentifier: String = BuildConfig.APPLICATION_ID,
    private val messageDelegate: MessageDelegate = MessageDelegate(connectionsClient),
) : Client,
    MessageSender by messageDelegate,
    MessageReceiver by messageDelegate {

    private val _connections = MutableStateFlow(emptyList<ClientConnection>())
    override val connections: Flow<List<ClientConnection>> = _connections

    override fun getConnections(): List<ClientConnection> {
        return _connections.value
    }

    override fun startDiscovery() {
        connectionsClient.stopAllEndpoints()

        val connections = mutableListOf<ClientConnection>()

        connectionsClient.startDiscovery(
            applicationIdentifier,
            object : EndpointDiscoveryCallback() {
                override fun onEndpointFound(
                    endpointId: String,
                    endpointInfo: DiscoveredEndpointInfo
                ) {
                    connections.add(ClientConnection.Found(endpointId, endpointInfo.endpointName))

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

    override fun connect(connection: ClientConnection) {
        // As long we are automatically calling acceptConnection below
        // it's fine to update connections immediately
        // Move to Requested state, server needs to approve connection too
        val immediateUpdate = getConnections().toMutableList()
        immediateUpdate.removeAll { it.endpointId == connection.endpointId }
        immediateUpdate.add(
            ClientConnection.Requested(
                connection.endpointId,
                connection.endpointName
            )
        )
        _connections.tryEmit(immediateUpdate)

        connectionsClient.requestConnection(
            userIdentifier,
            connection.endpointId,
            object : ConnectionLifecycleCallback() {
                override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
                    // we selected to connect, therefore we can directly accept that connection
                    connectionsClient.acceptConnection(endpointId, messageDelegate)
                }

                override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                    val connections = getConnections().toMutableList()
                    val old = connections.first { it.endpointId == endpointId }
                    val new = if (result.status.isSuccess) {
                        ClientConnection.Connected(endpointId, old.endpointName)
                    } else {
                        ClientConnection.Failure(endpointId, old.endpointName)
                    }

                    connections.remove(old)
                    connections.add(new)

                    _connections.tryEmit(connections)
                }

                override fun onDisconnected(endpointId: String) {
                    val connections = _connections.value.toMutableList()
                    val old = connections.first { it.endpointId == endpointId }
                    val new = ClientConnection.Failure(endpointId, old.endpointName)

                    connections.remove(old)
                    connections.add(new)

                    _connections.tryEmit(connections)
                }
            })
    }
}
