package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.BuildConfig
import at.aau.edu.wizards.api.Server
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ServerImpl(
    private val connectionsClient: ConnectionsClient,
    private val payloadCallback: PayloadCallback,
    private val userIdentifier: String = GENERATED_NAME,
    private val applicationIdentifier: String = BuildConfig.APPLICATION_ID,
) : Server {

    private val _connections = MutableStateFlow<List<Server.Connection>>(emptyList())
    override val connections: StateFlow<List<Server.Connection>> = _connections.asStateFlow()

    override fun getConnections(): List<Server.Connection> {
        return _connections.value
    }

    override fun startBroadcasting() {
        val connections = mutableListOf<Server.Connection>()

        connectionsClient.startAdvertising(
            userIdentifier,
            applicationIdentifier,
            object : ConnectionLifecycleCallback() {
                override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
                    connections.add(Server.Connection.ClientRequest(endpointId, info.endpointName))

                    _connections.tryEmit(connections)
                }

                override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                    val old = connections.first { it.endpointId == endpointId }
                    val new = if (result.status.isSuccess) {
                        Server.Connection.Connected(endpointId, old.endpointName)
                    } else {
                        Server.Connection.Failure(endpointId, old.endpointName)
                    }

                    connections.remove(old)
                    connections.add(new)

                    _connections.tryEmit(connections)
                }

                override fun onDisconnected(endpointId: String) {
                    val old = connections.first { it.endpointId == endpointId }
                    val new = Server.Connection.Failure(endpointId, old.endpointName)

                    connections.remove(old)
                    connections.add(new)

                    _connections.tryEmit(connections)
                }
            },
            advertisingOptions
        )
    }

    override fun stopBroadcasting() {
        connectionsClient.stopAdvertising()
    }

    override fun acceptClientRequest(connection: Server.Connection) {
        connectionsClient.acceptConnection(connection.endpointId, payloadCallback)
    }

    override fun declineClientRequest(connection: Server.Connection) {
        connectionsClient.rejectConnection(connection.endpointId)
    }
}