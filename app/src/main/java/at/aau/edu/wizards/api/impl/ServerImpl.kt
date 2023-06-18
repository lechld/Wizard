package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.BuildConfig
import at.aau.edu.wizards.USERDATA_AVATAR
import at.aau.edu.wizards.USERDATA_USERNAME
import at.aau.edu.wizards.USERDATA_UUID
import at.aau.edu.wizards.api.MessageReceiver
import at.aau.edu.wizards.api.MessageSender
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ServerImpl(
    private val connectionsClient: ConnectionsClient,
    private val myEndpointName: String = USERDATA_USERNAME + ":" + USERDATA_AVATAR,
    private val applicationIdentifier: String = BuildConfig.APPLICATION_ID,
    private val messageDelegate: MessageDelegate = MessageDelegate(connectionsClient),
) : Server,
    MessageSender by messageDelegate,
    MessageReceiver by messageDelegate {

    private val _connections = MutableStateFlow<List<ServerConnection>>(emptyList())
    override val connections: StateFlow<List<ServerConnection>> = _connections.asStateFlow()

    override fun getConnectionsSync(): List<ServerConnection> {
        return _connections.value
    }

    override fun startBroadcasting() {
        connectionsClient.stopAllEndpoints()

        val connections = mutableListOf<ServerConnection>()

        connectionsClient.startAdvertising(
            myEndpointName,
            applicationIdentifier,
            object : ConnectionLifecycleCallback() {
                override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
                    connections.add(ServerConnection.ClientRequest(endpointId, info.endpointName))

                    _connections.tryEmit(connections)
                }

                override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                    val old = connections.first { it.endpointId == endpointId }
                    val new = if (result.status.isSuccess) {
                        ServerConnection.Connected(endpointId, old.endpointName)
                    } else {
                        ServerConnection.Failure(endpointId, old.endpointName)
                    }

                    connections.remove(old)
                    connections.add(new)

                    _connections.tryEmit(connections)
                }

                override fun onDisconnected(endpointId: String) {
                    val old = connections.first { it.endpointId == endpointId }
                    val new = ServerConnection.Failure(endpointId, old.endpointName)

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

    override fun acceptClientRequest(connection: ServerConnection) {
        connectionsClient.acceptConnection(connection.endpointId, messageDelegate)
    }

    override fun declineClientRequest(connection: ServerConnection) {
        connectionsClient.rejectConnection(connection.endpointId)
    }
}