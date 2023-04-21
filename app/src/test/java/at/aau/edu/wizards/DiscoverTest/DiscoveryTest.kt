import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.ui.discover.DiscoverItemFactory
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.verify

class DiscoveryTest {

    @Mock
    lateinit var client: Client
    lateinit var discoverItemFactory: DiscoverItemFactory

    @Before
    fun setup() {
        //MockKAnnotations.init(this)
    }

    @Test
    fun startDiscovery() {
        // Given
        val discovery = DiscoverViewModel(client, discoverItemFactory)

        // When
        discovery.startDiscovery()

        // Then
        verify { client.startDiscovery() }
    }
}