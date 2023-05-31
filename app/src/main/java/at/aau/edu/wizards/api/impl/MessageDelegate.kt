package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.api.EMPTY_DATA
import at.aau.edu.wizards.api.MessageReceiver
import at.aau.edu.wizards.api.MessageSender
import at.aau.edu.wizards.api.model.Connection
import at.aau.edu.wizards.api.model.Data
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.nio.charset.Charset

// FIXME meaningful message content (eg, GameCommand), or message parsing in dedicated class for maintainability
class MessageDelegate(
    private val connectionsClient: ConnectionsClient,
    private val encoding: Charset = Charsets.UTF_8,
) : MessageReceiver, MessageSender, PayloadCallback() {

    // region MessageReceiver

    private val _messages = MutableStateFlow(EMPTY_DATA)
    override val messages: Flow<Data> = _messages

    // endregion

    // region MessageSender

    override fun send(connection: Connection, data: String) {
        val payload = Payload.fromBytes(data.toByteArray(encoding))

        connectionsClient.sendPayload(connection.endpointId, payload)
    }

    // endregion

    // region PayloadCallback

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        val value = payload.asBytes()?.let { bytes ->
            String(bytes, encoding)
        } ?: ""
        val data = Data(endpointId, value)

        _messages.tryEmit(data)
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
        // Stub
    }

    // endregion
}