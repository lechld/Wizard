package at.aau.edu.wizards.ui.scoreboard.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.ItemScoreboardBinding
import at.aau.edu.wizards.ui.scoreboard.Scoreboard

class ScoreboardItemViewHolder(
    private val binding: ItemScoreboardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Scoreboard) {
        binding.avatarImage.setImageResource(R.drawable.icon1) //TODO Player-Avatar
        binding.tvPlayerName.text = item.playerName
        binding.tvScore.text = item.score.toString()
    }
}