package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class GameBoardViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<Cards>>()
    val cards: LiveData<List<Cards>> = _cards

    open fun sendMessage(move: String) {}


}


