package at.aau.edu.wizards.ui.popup

import androidx.lifecycle.*
import at.aau.edu.wizards.gameModel.GameModelListener

class PopUpViewModel(
    val listener: GameModelListener
) : ViewModel() {

    private val _winningcard = MutableLiveData<ArrayList<PopUp>>()
    val winningcard: LiveData<ArrayList<PopUp>> = _winningcard.distinctUntilChanged()


    init {
        val winningcard = ArrayList<PopUp>()

        for (player in 0 until listener.numberOfPlayers) {
            winningcard.add(
                PopUp(
                    listener.getIconFromId(player)
                )
            )
        }
        _winningcard.value = winningcard
    }

    class Factory(
        private val listener: GameModelListener
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(PopUpViewModel::class.java)) {
                PopUpViewModel(listener) as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}