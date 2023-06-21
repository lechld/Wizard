package at.aau.edu.wizards.ui.scoreboard.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemScoreboardBinding
import at.aau.edu.wizards.ui.scoreboard.Scoreboard

class ScoreboardAdapter : ListAdapter<Scoreboard, ScoreboardItemViewHolder>(DiffUtlCallback()) {

    lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardItemViewHolder {
        context = parent.context
        val from = LayoutInflater.from(context)
        val binding = ItemScoreboardBinding.inflate(from, parent, false)
        return ScoreboardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreboardItemViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }

    private class DiffUtlCallback : DiffUtil.ItemCallback<Scoreboard>() {
        override fun areItemsTheSame(oldItem: Scoreboard, newItem: Scoreboard): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Scoreboard, newItem: Scoreboard): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}