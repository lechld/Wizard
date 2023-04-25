package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.*
import at.aau.edu.wizards.R
import kotlinx.coroutines.launch

class GameBoardViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<Cards>>()
    val cards: LiveData<List<Cards>> = _cards

    init {
        viewModelScope.launch {
            _cards.postValue(
                listOf(
                    Cards(R.drawable.card),
                    Cards(R.drawable.card),
                    Cards(R.drawable.card),
                    Cards(R.drawable.card)
                )
            )
        }
    }

    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(GameBoardViewModel::class.java)) {
                // creates the instance and provides someDependency
                GameBoardViewModel() as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}


