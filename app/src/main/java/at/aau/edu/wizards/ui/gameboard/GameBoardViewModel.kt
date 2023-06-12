package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.*
import at.aau.edu.wizards.R
import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.GameModelCard
import at.aau.edu.wizards.gameModel.GameModelListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    protected val _scoreboard = MutableLiveData<Boolean>()
    val scoreboard: LiveData<Boolean> = _scoreboard.distinctUntilChanged()

    protected val _popup = MutableLiveData<Boolean>()
    val popup: LiveData<Boolean> = _popup.distinctUntilChanged()

    private val _winningcard = MutableLiveData<GameModelListener.WinningCardPopUp>()
    val winningcard : LiveData<GameModelListener.WinningCardPopUp> = _winningcard.distinctUntilChanged()

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

        _winningcard.postValue(model.listener.winningCardPopUp)
        if (winningcard.value != null) {
            if (winningcard.value!!.visible) {
                viewModelScope.launch {
                    popUpVisibilityDelay()
                }
            }
        }
    }

    private suspend fun popUpVisibilityDelay() {
        delay(3000)
        _winningcard.value = GameModelListener.WinningCardPopUp(winningcard.value!!.lastCardWon, winningcard.value!!.lastPlayerWon, false)
    }

    private fun buildGuessList(model: GameModel): List<Int> {
        val guesses = mutableListOf<Int>()

        repeat(model.listener.getRound() + 1) {
            guesses.add(it)
        }

        return guesses
    }

    fun updateGuess(guessInt: Int) {

        gameModel.updateGuessCount(guessInt)

    }

    fun getBuildGuess(): Array<CharSequence> {

        val charSequenceArray: Array<CharSequence> =
            buildGuessList(gameModel).map { it.toString() }.toTypedArray()

        return charSequenceArray
    }

    fun getIconFromId(icon: Int): Int {
        return when (icon) {
            1 -> R.drawable.icon1
            2 -> R.drawable.icon2
            3 -> R.drawable.icon3
            4 -> R.drawable.icon4
            5 -> R.drawable.icon5
            6 -> R.drawable.icon6
            7 -> R.drawable.icon7
            8 -> R.drawable.icon8
            9 -> R.drawable.icon9
            10 -> R.drawable.icon10
            11 -> R.drawable.icon11
            12 -> R.drawable.icon12
            13 -> R.drawable.icon13
            14 -> R.drawable.icon14
            15 -> R.drawable.icon15
            16 -> R.drawable.icon16
            17 -> R.drawable.icon17
            18 -> R.drawable.icon18
            else -> R.drawable.icon19
        }
    }
}

