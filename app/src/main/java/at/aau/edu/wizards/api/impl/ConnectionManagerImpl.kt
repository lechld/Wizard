package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.BuildConfig
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.ConnectionManager
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.Data
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class ConnectionManagerImpl(
    private val connectionsClient: ConnectionsClient,
    private val userIdentifier: String = GENERATED_NAME,
    private val applicationIdentifier: String = BuildConfig.APPLICATION_ID,
) : ConnectionManager {

    private val _messages = MutableStateFlow<Data?>(null)
    override val messages: Flow<Data?> = _messages

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val value = payload.asBytes()?.let { bytes ->
                String(bytes, Charsets.UTF_8)
            } ?: ""
            val data = Data(endpointId, value)

            _messages.tryEmit(data)
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            // Stub
        }
    }

    override val client: Client by lazy {
        ClientImpl(
            connectionsClient = connectionsClient,
            payloadCallback = payloadCallback,
            userIdentifier = userIdentifier,
            applicationIdentifier = applicationIdentifier
        )
    }

    override val server: Server by lazy {
        ServerImpl(
            connectionsClient = connectionsClient,
            payloadCallback = payloadCallback,
            userIdentifier = userIdentifier,
            applicationIdentifier = applicationIdentifier
        )
    }

    override fun send(endpointId: String, data: String) {
        val payload = Payload.fromBytes(data.toByteArray(Charsets.UTF_8))

        connectionsClient.sendPayload(endpointId, payload)
    }
}