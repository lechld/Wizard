package at.aau.edu.wizards.discover_testing

import at.aau.edu.wizards.api.impl.ClientImpl
import at.aau.edu.wizards.api.impl.MessageDelegate
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import com.google.android.gms.nearby.connection.ConnectionsClient
import org.junit.jupiter.api.Test
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class discovertesting {

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

    @Test
    fun testStartDiscoveryCalledOnUIInit() {
        val realClient = client// Echte Implementierung des Client-Objekts
        val spyClient = spy(realClient) // Spy-Objekt erstellen
        val viewModel = DiscoverViewModel(spyClient) // ViewModel mit Spy-Objekt erstellen
        viewModel.startDiscovery()
        verify(spyClient, times(1)).startDiscovery() // Überprüfen, ob die Methode aufgerufen wurde
    }
}