package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.api.EMPTY_DATA
import at.aau.edu.wizards.api.model.Connection
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.then
import org.mockito.kotlin.whenever
import java.nio.charset.Charset

internal class MessageDelegateTest {

    private val connectionsClient = mock<ConnectionsClient>()
    private val encoding: Charset = Charsets.UTF_8
    private val messageDelegate = MessageDelegate(connectionsClient, encoding)

    @OptIn(ExperimentalCoroutinesApi::class)
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

        // TODO: need to verify only arguments
        then(connectionsClient).should() .sendPayload(connection.endpointId, payload)
    }

    // When send, then connectionsclient sendpayload should be called

    // payload received with null bytes, with real bytes

    // transferupdate - assert nothing happens on mock

}