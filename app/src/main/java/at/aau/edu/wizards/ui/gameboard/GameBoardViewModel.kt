package at.aau.edu.wizards.ui.gameboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.aau.edu.wizards.gameModel.GameModelCard

abstract class GameBoardViewModel : ViewModel() {

    protected val mutableCards = MutableLiveData<ArrayList<GameModelCard>>()
    val cards : LiveData<ArrayList<GameModelCard>> = mutableCards

    open fun sendMessage(move: String) {}


}


