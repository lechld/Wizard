package at.aau.edu.wizards.ui.scoreboard.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemScoreboardBinding
import at.aau.edu.wizards.ui.scoreboard.Scoreboard

class ScoreboardAdapter : ListAdapter<Scoreboard, ScoreboardItemViewHolder>(DiffUtlCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemScoreboardBinding.inflate(from, parent, false)
        return ScoreboardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreboardItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffUtlCallback : DiffUtil.ItemCallback<Scoreboard>() {
        override fun areItemsTheSame(oldItem: Scoreboard, newItem: Scoreboard): Boolean {
            return oldItem.avatar == newItem.avatar
            return oldItem.points == newItem.points
        }

        override fun areContentsTheSame(oldItem: Scoreboard, newItem: Scoreboard): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}