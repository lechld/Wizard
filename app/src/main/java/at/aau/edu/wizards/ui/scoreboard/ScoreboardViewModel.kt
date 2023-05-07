package at.aau.edu.wizards.ui.scoreboard

import androidx.lifecycle.*
import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.MessageReceiver
import at.aau.edu.wizards.api.MessageSender
import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.GameModelListener
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import at.aau.edu.wizards.ui.scoreboard.Scoreboard
import kotlinx.coroutines.launch

class ScoreboardViewModel(
    val listener: GameModelListener
) : ViewModel() {

    private val _score = MutableLiveData<ArrayList<Scoreboard>>()
    val score : LiveData<ArrayList<Scoreboard>> = _score.distinctUntilChanged()

    private val _playerName = MutableLiveData<ArrayList<Scoreboard>>()
    val playerName : LiveData<ArrayList<Scoreboard>> = _playerName.distinctUntilChanged()

    init {
        val scorelist = ArrayList<Scoreboard>()


        for (player in 0 until listener.numberOfPlayers) {
            scorelist.add(Scoreboard(getIconFromId(player), listener.getNameOfPlayer(player), listener.getCurrentScoreOfPlayer(player)))
        }
        _score.value = scorelist
    }

    class Factory(
        private val listener: GameModelListener
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(ScoreboardViewModel::class.java)) {
                ScoreboardViewModel(listener) as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }


    fun getIconFromId(icon:Int) : Int{
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




