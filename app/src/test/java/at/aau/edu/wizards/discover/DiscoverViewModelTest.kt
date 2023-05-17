package at.aau.edu.wizards.discover

import at.aau.edu.wizards.InstantTaskExecutorExtension
import at.aau.edu.wizards.TestCoroutineDispatcherExtension
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.model.ClientConnection
import at.aau.edu.wizards.ui.discover.DiscoverItem
import at.aau.edu.wizards.ui.discover.DiscoverItemFactory
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExtendWith(
    value = [
        TestCoroutineDispatcherExtension::class, // Execute coroutines in sync
        InstantTaskExecutorExtension::class, // Execute lifecycle tasks in sync
    ]
)
class DiscoverViewModelTest {

    @Test
    fun `given view model, when client posts new connections, assert they are reflected inside items`() =
        runTest {
            val client = mock<Client> {
                on { messages } doReturn emptyFlow()
            }
            val factory = mock<DiscoverItemFactory>()
            val connection = mock<ClientConnection>()
            val connections = listOf(connection)
            val discoverItem = mock<DiscoverItem>()
            val discoverItems = listOf(discoverItem)

            whenever(client.connections).doReturn(flowOf(connections))
            whenever(factory.create(connections)).doReturn(discoverItems)

            val viewModel = DiscoverViewModel(client, factory)
            val items = viewModel.items.first()

            Assertions.assertEquals(discoverItems, items)
        }

}