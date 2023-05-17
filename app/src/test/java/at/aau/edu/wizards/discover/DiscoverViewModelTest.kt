package at.aau.edu.wizards.discover

import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.ui.discover.DiscoverItem
import at.aau.edu.wizards.ui.discover.DiscoverItemFactory
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import at.aau.edu.wizards.ui.lobby.LobbyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class DiscoverViewModelTest {

    private val client = mock<Client>()
    private val discoverItemFactory = mock<DiscoverItemFactory>()

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

    /*
    - item from conection
    - init when start command , when no command(not start)

    - Factory separate tested

     */

    /*
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Connections test Items LiveData Initialized With DiscoverItems`() = runTest {
        val clientConnection = mock<ClientConnection>()
        val listClientConnection = listOf(clientConnection)
        val flowOfClient = flowOf(listClientConnection)
        val expectedResult = mock<DiscoverItem>()
        val listExpectedResult  = listOf(expectedResult)

        whenever((discoverItemFactory.create(listClientConnection))).doReturn(listExpectedResult)
        whenever(client.connections).doReturn(flowOfClient)

        val viewModel = DiscoverViewModel(client, discoverItemFactory)

        val result = viewModel.items.value
        Assertions.assertEquals(listExpectedResult, result)
    }

     */

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

        then(client).should().getConnections()
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

            Assertions.assertTrue(exceptedResult is DiscoverViewModel)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `Factory create DiscoveryViewModel is not DiscoveryViewModel`() = runTest {

            val factory = DiscoverViewModel.Factory(client)
            try {

                val exceptedResult = factory.create(LobbyViewModel::class.java)

            } catch (e: IllegalStateException) {

                val result = e
                Assertions.assertEquals(
                    "Wrong Factory for instantiating java.lang.Class", result.message
                )
            }
        }
    }
}
