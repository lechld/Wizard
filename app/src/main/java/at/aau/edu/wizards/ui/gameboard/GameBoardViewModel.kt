package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.MessageReceiver
import at.aau.edu.wizards.api.MessageSender
import kotlinx.coroutines.launch

abstract class GameBoardViewModel(
    private val messageReceiver: MessageReceiver,
    private val messageSender: MessageSender,
) : ViewModel() {

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
}


