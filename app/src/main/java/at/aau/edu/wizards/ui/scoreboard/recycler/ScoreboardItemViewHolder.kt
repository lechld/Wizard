package at.aau.edu.wizards.ui.scoreboard.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemScoreboardBinding
import at.aau.edu.wizards.ui.scoreboard.Scoreboard

class ScoreboardItemViewHolder(
    private val binding: ItemScoreboardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Scoreboard) {
        val s = buildString {
            append("Total Score " + "\n" + item.score.toString())
        }
        binding.avatarImage.setImageResource(item.playerIcon)
        binding.tvPlayerName.text = item.playerName
        binding.tvScore.text = s
    }
}