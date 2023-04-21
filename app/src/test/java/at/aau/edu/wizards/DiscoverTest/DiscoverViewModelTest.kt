package at.aau.edu.wizards.DiscoverTest

import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.model.ClientConnection
import at.aau.edu.wizards.ui.discover.DiscoverItem
import at.aau.edu.wizards.ui.discover.DiscoverItemFactory
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class DiscoverViewModelTest {

    @Test
    fun testItemsLiveDataInitializedWithDiscoverItems() {
        val mockClient = mock(Client::class.java)
        val viewModel = DiscoverViewModel(mockClient)
        assertTrue(viewModel.items.value is List<DiscoverItem>)
    }

    @Test
    fun testStartDiscoveryCalledOnUIInit() {
        val mockClient = mock(Client::class.java)
        val viewModel = DiscoverViewModel(mockClient)
        viewModel.startDiscovery()
        verify(mockClient, times(1)).startDiscovery()
    }

    @Test
    fun testStopDiscoveryCalledOnUIDestroy() {
        val mockClient = mock(Client::class.java)
        val viewModel = DiscoverViewModel(mockClient)
        viewModel.stopDiscovery()
        verify(mockClient, times(1)).stopDiscovery()
    }

    @Test
    fun testConnectEndpointCalledOnPendingClick() {
        val mockClient = mock(Client::class.java)
        val viewModel = DiscoverViewModel(mockClient)
        val mockPendingConnection = mock(ClientConnection.Found::class.java)
        val mockPendingDiscoverItem = DiscoverItem.Pending(mockPendingConnection)
        viewModel.connectEndpoint(mockPendingDiscoverItem)
        verify(mockClient, times(1)).connect(mockPendingConnection)
    }

    @Test
    fun testDiscoverItemFactoryCreateMethodCalledOnConnectionsUpdate() {
        val mockClient = mock(Client::class.java)
        val mockFactory = mock(DiscoverItemFactory::class.java)
        val mockConnectionList = listOf(mock(ClientConnection::class.java))
        val viewModel = DiscoverViewModel(mockClient, mockFactory)

        viewModel.items.observeForever { }

        verify(mockFactory, times(1)).create(mockConnectionList)
    }



}
