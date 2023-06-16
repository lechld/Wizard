package at.aau.edu.wizards.ui.scoreboard.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.ItemScoreboardBinding
import at.aau.edu.wizards.ui.scoreboard.Scoreboard

class ScoreboardItemViewHolder(
    private val binding: ItemScoreboardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Scoreboard, context: Context) {
        binding.avatarImage.setImageResource(item.playerIcon)
        binding.tvPlayerName.text = item.playerName
        binding.tvScore.text = context.getString(R.string.scoreboard_score, item.score)
        binding.tvPlayerGuess.text = context.getString(R.string.scoreboard_guess, item.playerGuess)
    }
}