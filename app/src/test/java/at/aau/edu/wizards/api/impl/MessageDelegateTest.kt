package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.api.EMPTY_DATA
import at.aau.edu.wizards.api.model.Connection
import at.aau.edu.wizards.api.model.Data
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.nio.charset.Charset

internal class MessageDelegateTest {

    private val connectionsClient = mock<ConnectionsClient>()
    private val encoding: Charset = Charsets.UTF_8
    private val messageDelegate = MessageDelegate(connectionsClient, encoding)

    @Test
    fun `given plain delegate, on collecting messages, assert EMPTY_DATA is delivered`() = runTest {
        val initial = messageDelegate.messages.first()

        Assertions.assertEquals(initial, EMPTY_DATA)
    }

    @Test
    fun `given delegate, on sending, assert data is send properly with client as payload`() {
        val connection = object : Connection {
            override val endpointId: String = "some id"
            override val endpointName: String = "some name"
        }
        val data = "some data we send"
        val payload = Payload.fromBytes(data.toByteArray(encoding))

        // little bit tricky to set a doReturn. Let's use a mock. We don't rely on result anyhow.
        val taskResult = mock<Task<Void>>()

        whenever(connectionsClient.sendPayload(connection.endpointId, payload)).doReturn(taskResult)

        messageDelegate.send(connection, data)

        then(connectionsClient).should().sendPayload(
            eq(connection.endpointId),
            argThat { String(asBytes()!!, encoding) == data }
        )
    }

    @Test
    fun `given delegate, on receiving payload with null bytes, assert data is created with empty string`() =
        runTest {
            val endpointId = "some endpoint id"
            val payload = mock<Payload> {
                on { asBytes() } doReturn null
            }

            messageDelegate.onPayloadReceived(endpointId, payload)

            val message = messageDelegate.messages
                .first()

            val expected = Data(endpointId, "")

            Assertions.assertEquals(expected, message)
        }

    @Test
    fun `given delegate, on receiving payload with bytes, assert data is created with proper string`() =
        runTest {
            val endpointId = "some endpoint id"
            val value = "some value"
            val payload = Payload.fromBytes(value.toByteArray(encoding))

            messageDelegate.onPayloadReceived(endpointId, payload)

            val message = messageDelegate.messages
                .first()

            val expected = Data(endpointId, value)

            Assertions.assertEquals(expected, message)
        }

    @Test
    fun `given delegate, on payload transfer update, assert nothing happens`() {
        val endpointId = "some endpoint id"
        val payloadTransferUpdate = PayloadTransferUpdate.Builder().build()

        messageDelegate.onPayloadTransferUpdate(endpointId, payloadTransferUpdate)

        then(connectionsClient).shouldHaveNoInteractions()
    }
}