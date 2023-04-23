package at.aau.edu.wizards.DiscoverTest

import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.impl.ClientImpl
import at.aau.edu.wizards.api.impl.MessageDelegate
import at.aau.edu.wizards.api.model.ClientConnection
import at.aau.edu.wizards.ui.discover.MainscreenItem
import at.aau.edu.wizards.ui.discover.MainscreenItemFactory
import at.aau.edu.wizards.ui.discover.MainscreenViewModel
import com.google.android.gms.nearby.connection.ConnectionsClient
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class MainscreenViewModelTest {
    private val connectionsClient = org.mockito.kotlin.mock<ConnectionsClient>()
    private val userIdentifier = "user identifier"
    private val applicationIdentifier = "application identifier"
    private val messageDelegate = org.mockito.kotlin.mock<MessageDelegate>()

    private val client = ClientImpl(
        connectionsClient,
        userIdentifier,
        applicationIdentifier,
        messageDelegate
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testItemsLiveDataInitializedWithDiscoverItems() {
        val viewModel = MainscreenViewModel(client)
        assertFalse(viewModel.items.isInitialized)
    }

    @Test
    fun testStartDiscoveryCalledOnUIInit() {
        val realClient = client// Echte Implementierung des Client-Objekts
        val spyClient = spy(realClient) // Spy-Objekt erstellen
        val viewModel = MainscreenViewModel(spyClient) // ViewModel mit Spy-Objekt erstellen
        viewModel.startDiscovery()
        verify(spyClient, times(1)).startDiscovery() // Überprüfen, ob die Methode aufgerufen wurde
    }

    @Test
    fun testStopDiscoveryCalledOnUIDestroy() {
        val realClient = client// Echte Implementierung des Client-Objekts
        val spyClient = spy(realClient) // Spy-Objekt erstellen
        val viewModel = MainscreenViewModel(spyClient) // ViewModel mit Spy-Objekt erstellen
        viewModel.startDiscovery()
        viewModel.stopDiscovery()
        verify(spyClient, times(1)).stopDiscovery()
    }

    @Test
    fun testDiscoverItemFactoryCreateMethodCalledOnConnectionsUpdateWithSpy() {
        //val mockClient = client
        val spyFactory = spy(MainscreenItemFactory::class.java)
        val mockConnectionList = listOf(mock(ClientConnection::class.java))

        spyFactory.create(mockConnectionList)

        verify(spyFactory, times(1)).create(mockConnectionList)
    }

    @Test
    fun testConnectEndpoint() {
        // Arrange
        val client = mock(Client::class.java)
        val connection = mock(ClientConnection.Found::class.java)
        val discoverItem = mock(MainscreenItem.Pending(connection)::class.java)

        val viewModel = MainscreenViewModel(client)

        // Act
        //testClass.connectEndpoint(discoverItem)
        viewModel.connectEndpoint(discoverItem)

        // Assert
        verify(client).connect(connection)
    }
}
