package at.aau.edu.wizards.lobby

import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.api.model.ServerConnection
import at.aau.edu.wizards.ui.lobby.LobbyItem
import at.aau.edu.wizards.ui.lobby.LobbyItemFactory
import at.aau.edu.wizards.ui.lobby.LobbyViewModel
import junit.framework.TestCase.assertFalse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class LobbyViewModelUnitTest {

    private val server = mock<Server>()
    private val lobbyItemFactory = mock<LobbyItemFactory>()
    private val viewModel = LobbyViewModel(server, lobbyItemFactory)

    @Test
    fun `Testing numPlayer adding human CPU player`() {
        viewModel.clicked(LobbyItem.AddCpu)
        assertEquals(2, viewModel.numPlayer)
    }

    @Test
    fun `Testing numPlayer adding human player`() {
        val requestedConnection = mock<ServerConnection.ClientRequest>()
        viewModel.clicked(LobbyItem.Requested(requestedConnection))
        assertEquals(2, viewModel.numPlayer)

    }

    @Test
    fun `Testing numPlayer adding human player and CUP player`() {
        val requestedConnection = mock<ServerConnection.ClientRequest>()

        viewModel.clicked(LobbyItem.Requested(requestedConnection))
        viewModel.clicked(LobbyItem.AddCpu)
        viewModel.clicked(LobbyItem.AddCpu)
        viewModel.clicked(LobbyItem.Requested(requestedConnection))
        viewModel.clicked(LobbyItem.AddCpu)
        viewModel.clicked(LobbyItem.AddCpu)

        assertEquals(6, viewModel.numPlayer)

    }

    @Test
    fun `Testing numPlayer over 6 player`() {
        val requestedConnection = mock<ServerConnection.ClientRequest>()

        assertFalse(viewModel.checkTooManyPlayer)

        viewModel.clicked(LobbyItem.Requested(requestedConnection))
        viewModel.clicked(LobbyItem.AddCpu)
        viewModel.clicked(LobbyItem.AddCpu)
        viewModel.clicked(LobbyItem.Requested(requestedConnection))
        viewModel.clicked(LobbyItem.AddCpu)
        viewModel.clicked(LobbyItem.AddCpu)
        viewModel.clicked(LobbyItem.AddCpu)

        assertTrue(viewModel.checkTooManyPlayer)

    }
}
