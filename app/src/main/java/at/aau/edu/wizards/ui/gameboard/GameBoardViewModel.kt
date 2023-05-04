package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.GameModelCard

abstract class GameBoardViewModel : ViewModel() {

    private val _cards = MutableLiveData<ArrayList<GameModelCard>>()
    val cards: LiveData<ArrayList<GameModelCard>> = _cards.distinctUntilChanged()

    private val _board = MutableLiveData<ArrayList<GameModelCard>>()
    val board: LiveData<ArrayList<GameModelCard>> = _board.distinctUntilChanged()

    private val _trump = MutableLiveData<GameModelCard>()
    val trump: LiveData<GameModelCard> = _trump.distinctUntilChanged()

    private val _headersWithCurrentPlayer = MutableLiveData<Pair<ArrayList<GameBoardHeader>, Int>>()
    val headersWithCurrentPlayer: LiveData<Pair<ArrayList<GameBoardHeader>, Int>> =
        _headersWithCurrentPlayer.distinctUntilChanged()

    private val _guess = MutableLiveData<List<Int>>()
    val guess: LiveData<List<Int>> = _guess.distinctUntilChanged()

    abstract fun sendMessage(move: String)

    abstract val gameModel: GameModel

    fun updateData(model: GameModel) {
        _headersWithCurrentPlayer.postValue(model.listener.headerList to model.listener.activePlayer)

        if (model.listener.guessing) {
            _guess.postValue(buildGuessList(model))
            _board.postValue(ArrayList())
        } else {
            _guess.postValue(emptyList())
            _board.postValue(model.listener.boardAsNewArray())
        }

        _cards.postValue(model.listener.getHandOfPlayer(model.localPlayer()))
        _trump.postValue(model.listener.trump)
    }

    private fun buildGuessList(model: GameModel): List<Int> {
        val guesses = mutableListOf<Int>()

        repeat(model.listener.getRound() + 1) {
            guesses.add(it)
        }

        return guesses
    }
}


