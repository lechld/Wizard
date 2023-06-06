package at.aau.edu.wizards.ui.popup

import androidx.lifecycle.*
import at.aau.edu.wizards.gameModel.GameModelListener

class PopUpViewModel(
    val listener: GameModelListener
) : ViewModel() {

    private val _winningcard = MutableLiveData<GameModelListener.WinningCardPopUp>()
    val winningcard: LiveData<GameModelListener.WinningCardPopUp> = _winningcard.distinctUntilChanged()


    init {

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