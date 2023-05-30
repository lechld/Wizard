package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.api.model.ServerConnection
import com.google.android.gms.common.api.Status
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionsClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.*

internal class ServerImplTest {

    private val connectionsClient = mock<ConnectionsClient>()
    private val userIdentifier = "user identifier"
    private val applicationIdentifier = "application identifier"
    private val messageDelegate = mock<MessageDelegate>()

    private val server = ServerImpl(
        connectionsClient = connectionsClient,
        userIdentifier = userIdentifier,
        applicationIdentifier = applicationIdentifier,
        messageDelegate = messageDelegate,
    )

    // Simplifies testing LifecycleCallback inside startBroadcasting
    private data class TestData(
        val endpointId: String,
        val endpointName: String,
        val connectionInfo: ConnectionInfo,
    )

    private val testData1 = TestData(
        endpointId = "endpointId1",
        endpointName = "endpointName1",
        connectionInfo = mock { on { it.endpointName } doReturn "endpointName1" },
    )

    private val testData2 = TestData(
        endpointId = "endpointId2",
        endpointName = "endpointName2",
        connectionInfo = mock { on { it.endpointName } doReturn "endpointName2" },
    )

    @Test
    fun `given server, without starting broadcasting, assert connections are empty`() = runTest {
        Assertions.assertEquals(emptyList<ServerConnection>(), server.connections.first())
        Assertions.assertEquals(emptyList<ServerConnection>(), server.getConnectionsSync())
    }

    // FIXME split into individual tests
    @Test
    fun `given server, on starting to broadcast, assert lifecycle callbacks are mapped properly to connections`() =
        runTest {
            val callbackCaptor = ArgumentCaptor.forClass(ConnectionLifecycleCallback::class.java)

            whenever(
                connectionsClient.startAdvertising(
                    any<String>(),
                    any(),
                    callbackCaptor.capture(),
                    any(),
                )
            ).doReturn(mock())

            server.startBroadcasting()

            val callback = callbackCaptor.value

            then(connectionsClient).should().startAdvertising(
                userIdentifier,
                applicationIdentifier,
                callback,
                advertisingOptions,
            )

            // Test onConnectionInitiated
            val expectedConnectionInit1 = ServerConnection.ClientRequest(
                endpointId = testData1.endpointId,
                endpointName = testData1.endpointName,
            )

            callback.onConnectionInitiated(testData1.endpointId, testData1.connectionInfo)

            Assertions.assertEquals(
                listOf(expectedConnectionInit1),
                server.connections.first()
            )

            val expectedConnectionInit2 = ServerConnection.ClientRequest(
                endpointId = testData2.endpointId,
                endpointName = testData2.endpointName,
            )

            callback.onConnectionInitiated(testData2.endpointId, testData2.connectionInfo)

            Assertions.assertEquals(
                listOf(expectedConnectionInit1, expectedConnectionInit2),
                server.connections.first()
            )
            Assertions.assertEquals(
                server.getConnectionsSync(),
                server.connections.first()
            )

            // Test onConnectionResult
            val expectedConnectionResult1 = ServerConnection.Connected(
                endpointId = testData1.endpointId,
                endpointName = testData1.endpointName,
            )

            val successStatus = mock<Status> {
                on { isSuccess } doReturn true
            }
            callback.onConnectionResult(
                testData1.endpointId,
                mock { on { status } doReturn successStatus }
            )

            Assertions.assertEquals(
                listOf(expectedConnectionInit2, expectedConnectionResult1),
                server.connections.first()
            )
            Assertions.assertEquals(
                server.getConnectionsSync(),
                server.connections.first()
            )

            val expectedConnectionResult2 = ServerConnection.Failure(
                endpointId = testData2.endpointId,
                endpointName = testData2.endpointName,
            )

            val failureStatus = mock<Status> {
                on { isSuccess } doReturn false
            }
            callback.onConnectionResult(
                testData2.endpointId,
                mock { on { status } doReturn failureStatus }
            )

            Assertions.assertEquals(
                listOf(expectedConnectionResult1, expectedConnectionResult2),
                server.connections.first()
            )
            Assertions.assertEquals(
                server.getConnectionsSync(),
                server.connections.first()
            )

            // Test onDisconnected
            val expectedDisconnectionResult = ServerConnection.Failure(
                endpointId = testData1.endpointId,
                endpointName = testData1.endpointName,
            )

            callback.onDisconnected(testData1.endpointId)

            Assertions.assertEquals(
                listOf(expectedConnectionResult2, expectedDisconnectionResult),
                server.connections.first()
            )
            Assertions.assertEquals(
                server.getConnectionsSync(),
                server.connections.first()
            )
        }

    @Test
    fun `given server, on stopping broadcasting, assert stop is delegated to client`() {
        server.stopBroadcasting()

        then(connectionsClient).should().stopAdvertising()
    }

    @Test
    fun `given server, on accepting request, assert accept is delegated to client`() {
        whenever(connectionsClient.acceptConnection(any(), any())).doReturn(mock())

        val endpointId = "endpoint id"
        val connection = ServerConnection.ClientRequest(endpointId, "endpoint name")

        server.acceptClientRequest(connection)

        then(connectionsClient).should().acceptConnection(endpointId, messageDelegate)
    }

    @Test
    fun `given server, on declining request, assert decline is delegated to client`() {
        val endpointId = "endpoint id"
        val connection = ServerConnection.ClientRequest(endpointId, "endpoint name")

        server.declineClientRequest(connection)

        then(connectionsClient).should().rejectConnection(endpointId)
    }
}