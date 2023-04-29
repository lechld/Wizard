package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.GameModelCard

abstract class GameBoardViewModel : ViewModel() {

    private val mutableCards = MutableLiveData<ArrayList<GameModelCard>>()
    val cards: LiveData<ArrayList<GameModelCard>> = mutableCards

    private val mutableBoard = MutableLiveData<ArrayList<GameModelCard>>()
    val board: LiveData<ArrayList<GameModelCard>> = mutableBoard

    private val mutableTrump = MutableLiveData<GameModelCard>()
    val trump: LiveData<GameModelCard> = mutableTrump

    open fun sendMessage(move: String) {}

    protected fun updateData(model: GameModel) {
        mutableCards.value = model.listener.getHandOfPlayer(model.localPlayer())
        mutableBoard.value = model.listener.getBoard()
        mutableTrump.value = model.listener.trump
    }
}


