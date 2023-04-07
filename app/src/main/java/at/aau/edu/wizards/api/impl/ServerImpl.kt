package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.BuildConfig
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.Connection
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

    private val _connections = MutableStateFlow<List<Connection>>(emptyList())
    override val connections: StateFlow<List<Connection>> = _connections.asStateFlow()

    override fun getConnections(): List<Connection> {
        return _connections.value
    }

    override fun startBroadcasting() {
        connectionsClient.startAdvertising(
            userIdentifier,
            applicationIdentifier,
            object : ConnectionLifecycleCallback() {
                val connections = mutableListOf<Connection>()

                override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
                    connections.add(Connection.Pending(endpointId))

                    _connections.tryEmit(connections)
                }

                override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                    connections.remove(Connection.Pending(endpointId))

                    if (result.status.isSuccess) {
                        connections.add(Connection.Success(endpointId))
                    }

                    _connections.tryEmit(connections)
                }

                override fun onDisconnected(endpointId: String) {
                    connections.remove(Connection.Pending(endpointId))
                    connections.remove(Connection.Success(endpointId))
                    connections.add(
                        Connection.Failure(endpointId, IllegalStateException("disconnected"))
                    )

                    _connections.tryEmit(connections)
                }
            },
            advertisingOptions
        )
    }

    override fun stopBroadcasting() {
        connectionsClient.stopAdvertising()
    }

    override fun acceptPendingConnection(endpointId: String) {
        connectionsClient.acceptConnection(endpointId, payloadCallback)
    }

    override fun declinePendingConnection(endpointId: String) {
        connectionsClient.rejectConnection(endpointId)
    }
}