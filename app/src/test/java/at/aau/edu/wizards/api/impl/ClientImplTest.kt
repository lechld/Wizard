package at.aau.edu.wizards.api.impl

import at.aau.edu.wizards.api.model.ClientConnection
import com.google.android.gms.common.api.Status
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.*

internal class ClientImplTest {

    private val connectionsClient = mock<ConnectionsClient>()
    private val userIdentifier = "user identifier"
    private val applicationIdentifier = "application identifier"
    private val messageDelegate = mock<MessageDelegate>()

    private val client = ClientImpl(
        connectionsClient,
        userIdentifier,
        applicationIdentifier,
        messageDelegate
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given client, without starting discovery, assert connections are empty`() = runTest {
        Assertions.assertEquals(emptyList<ClientConnection>(), client.connections.first())
        Assertions.assertEquals(emptyList<ClientConnection>(), client.getConnections())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given client, on starting discovery, assert connections are mapped properly`() = runTest {
        val callbackCaptor = ArgumentCaptor.forClass(EndpointDiscoveryCallback::class.java)

        whenever(
            connectionsClient.startDiscovery(
                any(),
                callbackCaptor.capture(),
                any()
            )
        ).doReturn(mock())

        client.startDiscovery()

        val callback = callbackCaptor.value

        then(connectionsClient).should().startDiscovery(
            applicationIdentifier,
            callback,
            discoveryOptions
        )

        // Test onEndpointFound

        val endpointId = "endpoint id"
        val endpointName = "endpoint name"
        val endpointInfo = mock<DiscoveredEndpointInfo> {
            on { getEndpointName() } doReturn endpointName
        }
        val expected = ClientConnection.Found(endpointId, endpointName)

        callback.onEndpointFound(endpointId, endpointInfo)

        Assertions.assertEquals(
            listOf(expected),
            client.connections.first()
        )
        Assertions.assertEquals(
            listOf(expected),
            client.getConnections()
        )

        // Test onEndpointLost

        callback.onEndpointLost(endpointId)

        Assertions.assertEquals(
            emptyList<ClientConnection>(),
            client.connections.first()
        )
        Assertions.assertEquals(
            emptyList<ClientConnection>(),
            client.getConnections()
        )
    }

    @Test
    fun `given client, on stopping discovery, assert stop is delegated to connections client`() {
        client.stopDiscovery()
        then(connectionsClient).should().stopDiscovery()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given client, on connect with initialised, assert connections are properly reflected`() =
        runTest {
            val callbackCaptor = ArgumentCaptor.forClass(ConnectionLifecycleCallback::class.java)
            val endpointId = "endpoint id"
            val endpointName = "endpoint name"
            val toConnect = ClientConnection.Found(endpointId, endpointName)
            val expected = ClientConnection.Requested(endpointId, endpointName)

            whenever(
                connectionsClient.requestConnection(
                    any<String>(),
                    any(),
                    callbackCaptor.capture()
                )
            ).doReturn(mock())

            client.connect(toConnect)

            val callback = callbackCaptor.value

            then(connectionsClient).should().requestConnection(userIdentifier, endpointId, callback)

            whenever(
                connectionsClient.acceptConnection(
                    endpointId,
                    messageDelegate
                )
            ).doReturn(mock())

            callback.onConnectionInitiated(endpointId, mock())

            then(connectionsClient).should().acceptConnection(endpointId, messageDelegate)

            Assertions.assertEquals(
                listOf(expected),
                client.connections.first()
            )
            Assertions.assertEquals(
                listOf(expected),
                client.getConnections()
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given client, on connect with success result, assert connections are properly reflected`() =
        runTest {
            val callbackCaptor = ArgumentCaptor.forClass(ConnectionLifecycleCallback::class.java)
            val endpointId = "endpoint id"
            val endpointName = "endpoint name"
            val toConnect = ClientConnection.Found(endpointId, endpointName)
            val expected = ClientConnection.Connected(endpointId, endpointName)

            whenever(
                connectionsClient.requestConnection(
                    any<String>(),
                    any(),
                    callbackCaptor.capture()
                )
            ).doReturn(mock())

            client.connect(toConnect)

            val callback = callbackCaptor.value

            then(connectionsClient).should().requestConnection(userIdentifier, endpointId, callback)

            whenever(
                connectionsClient.acceptConnection(
                    endpointId,
                    messageDelegate
                )
            ).doReturn(mock())

            val connectionStatus = mock<Status> {
                on { isSuccess } doReturn true
            }
            val connectionResult = mock<ConnectionResolution> {
                on { status } doReturn connectionStatus
            }

            callback.onConnectionResult(endpointId, connectionResult)

            Assertions.assertEquals(
                listOf(expected),
                client.connections.first()
            )
            Assertions.assertEquals(
                listOf(expected),
                client.getConnections()
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given client, on connect with failure result, assert connections are properly reflected`() =
        runTest {
            val callbackCaptor = ArgumentCaptor.forClass(ConnectionLifecycleCallback::class.java)
            val endpointId = "endpoint id"
            val endpointName = "endpoint name"
            val toConnect = ClientConnection.Found(endpointId, endpointName)
            val expected = ClientConnection.Failure(endpointId, endpointName)

            whenever(
                connectionsClient.requestConnection(
                    any<String>(),
                    any(),
                    callbackCaptor.capture()
                )
            ).doReturn(mock())

            client.connect(toConnect)

            val callback = callbackCaptor.value

            then(connectionsClient).should().requestConnection(userIdentifier, endpointId, callback)

            whenever(
                connectionsClient.acceptConnection(
                    endpointId,
                    messageDelegate
                )
            ).doReturn(mock())

            val connectionStatus = mock<Status> {
                on { isSuccess } doReturn false
            }
            val connectionResult = mock<ConnectionResolution> {
                on { status } doReturn connectionStatus
            }

            callback.onConnectionResult(endpointId, connectionResult)

            Assertions.assertEquals(
                listOf(expected),
                client.connections.first()
            )
            Assertions.assertEquals(
                listOf(expected),
                client.getConnections()
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given client, on connect with disconnection, assert connections are properly reflected`() =
        runTest {
            val callbackCaptor = ArgumentCaptor.forClass(ConnectionLifecycleCallback::class.java)
            val endpointId = "endpoint id"
            val endpointName = "endpoint name"
            val toConnect = ClientConnection.Found(endpointId, endpointName)
            val expected = ClientConnection.Failure(endpointId, endpointName)

            whenever(
                connectionsClient.requestConnection(
                    any<String>(),
                    any(),
                    callbackCaptor.capture()
                )
            ).doReturn(mock())

            client.connect(toConnect)

            val callback = callbackCaptor.value

            then(connectionsClient).should().requestConnection(userIdentifier, endpointId, callback)

            whenever(
                connectionsClient.acceptConnection(
                    endpointId,
                    messageDelegate
                )
            ).doReturn(mock())

            client.connect(toConnect)
            callback.onDisconnected(endpointId)

            Assertions.assertEquals(
                listOf(expected),
                client.connections.first()
            )
            Assertions.assertEquals(
                listOf(expected),
                client.getConnections()
            )
        }
}