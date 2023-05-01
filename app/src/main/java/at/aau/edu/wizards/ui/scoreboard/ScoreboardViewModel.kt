package at.aau.edu.wizards.ui.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.MessageReceiver
import at.aau.edu.wizards.api.MessageSender
import at.aau.edu.wizards.ui.scoreboard.Scoreboard
import kotlinx.coroutines.launch

abstract class ScoreboardViewModel(
    private val messageReceiver: MessageReceiver,
    private val messageSender: MessageSender,
) : ViewModel() {


    private val _points = MutableLiveData<List<Scoreboard>>()
    val points: LiveData<List<Scoreboard>> = _points

    private val _avatar = MutableLiveData<List<Scoreboard>>()
    val avatar: LiveData<List<Scoreboard>> = _avatar
    init {
        //TODO Player data from "GameModelListener/getAllScoresOfPlayer"
        viewModelScope.launch {
            _points.postValue(
                listOf(
                    Scoreboard(R.drawable.card, "144"),
                    Scoreboard(R.drawable.card, "125"),
                    Scoreboard(R.drawable.card, "180"),
                    Scoreboard(R.drawable.card, "180"),
                    Scoreboard(R.drawable.card, "145")
                )
            )
        }
    }
}




