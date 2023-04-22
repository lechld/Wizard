package at.aau.edu.wizards.DiscoverTest

import at.aau.edu.wizards.api.impl.ClientImpl
import at.aau.edu.wizards.api.impl.MessageDelegate
import at.aau.edu.wizards.api.model.ClientConnection
import at.aau.edu.wizards.ui.discover.DiscoverItem
import at.aau.edu.wizards.ui.discover.DiscoverItemFactory
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import at.aau.edu.wizards.ui.discover.recycler.DiscoverItemViewHolder
import com.google.android.gms.nearby.connection.ConnectionsClient
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class DiscoverViewModelTest {
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
        //val mockClient = mock(Client::class.java)
        val viewModel = DiscoverViewModel(client)
        assertFalse(viewModel.items.isInitialized)
    }

    @Test
    fun testStartDiscoveryCalledOnUIInit() {
        val realClient = client// Echte Implementierung des Client-Objekts
        val spyClient = spy(realClient) // Spy-Objekt erstellen
        val viewModel = DiscoverViewModel(spyClient) // ViewModel mit Spy-Objekt erstellen
        viewModel.startDiscovery()
        verify(spyClient, times(1)).startDiscovery() // Überprüfen, ob die Methode aufgerufen wurde
    }

    @Test
    fun testStopDiscoveryCalledOnUIDestroy() {
        val realClient = client// Echte Implementierung des Client-Objekts
        val spyClient = spy(realClient) // Spy-Objekt erstellen
        val viewModel = DiscoverViewModel(spyClient) // ViewModel mit Spy-Objekt erstellen
        viewModel.startDiscovery()
        viewModel.stopDiscovery()
        verify(spyClient, times(1)).stopDiscovery()
    }

    @Test
    fun testDiscoverItemFactoryCreateMethodCalledOnConnectionsUpdateWithSpy() {
        //val mockClient = client
        val spyFactory = spy(DiscoverItemFactory::class.java)
        val mockConnectionList = listOf(mock(ClientConnection::class.java))

        spyFactory.create(mockConnectionList)

        verify(spyFactory, times(1)).create(mockConnectionList)
    }
}
