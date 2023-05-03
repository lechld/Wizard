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

    private val mutableHeader = MutableLiveData<ArrayList<GameBoardHeader>>()
    val header: LiveData<ArrayList<GameBoardHeader>> = mutableHeader

    private val mutablePlayer = MutableLiveData<Int>()
    val player = mutablePlayer

    abstract fun sendMessage(move: String)

    abstract val gameModel : GameModel

    fun updateData(model: GameModel) {
        mutableCards.value = model.listener.getHandOfPlayer(model.localPlayer())
        mutableBoard.value = model.listener.board
        mutableTrump.value = model.listener.trump
        mutableHeader.value = model.listener.headerList
        mutablePlayer.value = model.listener.activePlayer
    }
}


