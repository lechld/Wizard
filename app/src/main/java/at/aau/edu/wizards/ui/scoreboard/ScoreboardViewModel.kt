package at.aau.edu.wizards.ui.scoreboard

import androidx.lifecycle.*
import at.aau.edu.wizards.gameModel.GameModelListener

class ScoreboardViewModel(
    val listener: GameModelListener
) : ViewModel() {

    private val _score = MutableLiveData<ArrayList<Scoreboard>>()
    val score: LiveData<ArrayList<Scoreboard>> = _score.distinctUntilChanged()

    init {
        val scoreList = ArrayList<Scoreboard>()

        for (player in 0 until listener.numberOfPlayers) {
            scoreList.add(
                Scoreboard(
                    listener.getIconFromId(listener.getIconOfPlayer(player)),
                    listener.getNameOfPlayer(player),
                    listener.getCurrentScoreOfPlayer(player),
                    listener.getCurrentGuessOfPlayer(player)
                )
            )
        }
        _score.value = scoreList
    }

    fun listOFPlayer(): Array<String> {

        var listPlayerName = mutableListOf<String>()

        for (player in 0 until listener.numberOfPlayers) {
            listPlayerName.add(listener.getNameOfPlayer(player))
        }
        var arrayPlayerName: Array<String> = listPlayerName.map { it.toString() }.toTypedArray()
        return arrayPlayerName
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
}