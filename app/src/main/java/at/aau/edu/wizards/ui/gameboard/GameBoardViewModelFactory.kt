package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.Server

class GameBoardViewModelFactory(
    private val asClient: Boolean,
    private val amountCpu: Int,
    private val server: Server,
    private val client: Client,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GameBoardViewModel::class.java)) {
            val viewModel = if (asClient) {
                ClientGameBoardViewModel(client)
            } else {
                ServerGameBoardViewModel(server, amountCpu)
            }
            viewModel as T
        } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
    }
}