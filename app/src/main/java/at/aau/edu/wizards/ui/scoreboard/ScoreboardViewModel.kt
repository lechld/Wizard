package at.aau.edu.wizards.ui.scoreboard

import androidx.lifecycle.*
import at.aau.edu.wizards.gameModel.GameModelListener

class ScoreboardViewModel(
    val listener: GameModelListener
) : ViewModel() {

    private val _score = MutableLiveData<ArrayList<Scoreboard>>()
    val score: LiveData<ArrayList<Scoreboard>> = _score.distinctUntilChanged()

    init {
        val scorelist = ArrayList<Scoreboard>()

        for (player in 0 until listener.numberOfPlayers) {
            scorelist.add(
                Scoreboard(
                    listener.getIconFromId(player),
                    listener.getNameOfPlayer(player),
                    listener.getCurrentScoreOfPlayer(player)
                )
            )
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
}