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

    open fun sendMessage(move: String) {}

    fun updateData(model: GameModel) {
        mutableCards.value = model.listener.getHandOfPlayer(model.localPlayer())
        mutableBoard.value = model.listener.getBoard()
        mutableTrump.value = model.listener.trump
        val headerList = ArrayList<GameBoardHeader>()
        for (player in 0 until model.listener.numberOfPlayers) {
            headerList.add(
                GameBoardHeader(
                    player,
                    model.listener.getIconOfPlayer(player),
                    model.listener.getNameOfPlayer(player),
                    model.listener.getCurrentGuessOfPlayer(player),
                    model.listener.getCurrentWins(player),
                    model.listener.getCurrentScoreOfPlayer(player),
                    model.listener.trump.getGameBoardTheme()
                )
            )
        }
        mutableHeader.value = headerList
        mutablePlayer.value = model.listener.activePlayer
    }

    open fun getGameModel(): GameModel {
        return GameModel(this) //OVERWRITE THIS
    }
}


