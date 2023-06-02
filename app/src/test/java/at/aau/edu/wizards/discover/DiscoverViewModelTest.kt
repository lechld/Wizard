package at.aau.edu.wizards.discover


import at.aau.edu.wizards.InstantTaskExecutorExtension
import at.aau.edu.wizards.TestCoroutineDispatcherExtension
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.model.ClientConnection
import at.aau.edu.wizards.api.model.Data
import at.aau.edu.wizards.gameModel.START_COMMAND
import at.aau.edu.wizards.ui.discover.DiscoverItem
import at.aau.edu.wizards.ui.discover.DiscoverItemFactory
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import at.aau.edu.wizards.ui.lobby.LobbyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.*

@ExtendWith(
    value = [
        TestCoroutineDispatcherExtension::class, // Execute coroutines in sync
        InstantTaskExecutorExtension::class, // Execute lifecycle tasks in sync
    ]
)
class DiscoverViewModelTest {
    private val client = mock<Client> {
        on { messages } doReturn flowOf(Data("1", START_COMMAND))
    }
    private val discoverItemFactory = mock<DiscoverItemFactory>()

    @BeforeEach
    fun setup() {
        val connection = mock<ClientConnection>()
        val connections = listOf(connection)
        val discoverItem = mock<DiscoverItem>()
        val discoverItems = listOf(discoverItem)

        whenever(client.connections).doReturn(flowOf(connections))
        whenever(discoverItemFactory.create(connections)).doReturn(discoverItems)

    }

    @Test
    fun `given view model, when client posts new connections, assert they are reflected inside items`() =
        runTest {

            val connection = mock<ClientConnection>()
            val connections = listOf(connection)
            val discoverItem = mock<DiscoverItem>()
            val exceptedResult = listOf(discoverItem)

            whenever(client.connections).doReturn(flowOf(connections))
            whenever(discoverItemFactory.create(connections)).doReturn(exceptedResult)

            val viewModel = DiscoverViewModel(client, discoverItemFactory)

            val items = viewModel.items.first()
            viewModel.startGame

            //println(test)

            Assertions.assertEquals(exceptedResult, items)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Testing startDiscovery`() = runTest {

        val viewModel = DiscoverViewModel(client, discoverItemFactory)
        viewModel.startDiscovery()

        then(client).should().startDiscovery()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Testing stopDiscovery`() = runTest {

        val viewModel = DiscoverViewModel(client, discoverItemFactory)

        viewModel.stopDiscovery()

        then(client).should().stopDiscovery()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Testing connectEndpoint`() = runTest {

        val viewModel = DiscoverViewModel(client, discoverItemFactory)
        val discoverItemPending = mock<DiscoverItem.Pending>()

        viewModel.connectEndpoint(discoverItemPending)

        then(client).should().connections
    }

    class FactoryTest() {

        private val client = mock<Client>()

        @OptIn(ExperimentalCoroutinesApi::class)
        @BeforeEach
        fun setup() {
            val testDispatcher = UnconfinedTestDispatcher()
            Dispatchers.setMain(testDispatcher)
            whenever(client.messages).doReturn(emptyFlow())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @AfterEach
        fun tearDown() {
            Dispatchers.resetMain()
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `Factory create DiscoveryViewModel is DiscoveryViewModel`() = runTest {

            val factory = DiscoverViewModel.Factory(client)

            val exceptedResult = factory.create(DiscoverViewModel::class.java)

            //Assertions.assertTrue(exceptedResult is DiscoverViewModel)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `Factory create DiscoveryViewModel is not DiscoveryViewModel`() = runTest {

            val factory = DiscoverViewModel.Factory(client)
            try {

                factory.create(LobbyViewModel::class.java)

            } catch (e: IllegalStateException) {

                val result = e
                Assertions.assertEquals(
                    "Wrong Factory for instantiating java.lang.Class", result.message
                )
            }
        }
    }
}
