package at.aau.edu.wizards.ui.gameboard.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemGameboardHeaderBinding
import at.aau.edu.wizards.ui.gameboard.GameBoardHeader

class GameBoardHeaderAdapter : ListAdapter<GameBoardHeader, GameBoardHeaderViewHolder>(
    DiffUtilCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameBoardHeaderViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemGameboardHeaderBinding.inflate(from, parent, false)
        return GameBoardHeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameBoardHeaderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<GameBoardHeader>() {
        override fun areItemsTheSame(
            oldItem: GameBoardHeader,
            newItem: GameBoardHeader
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: GameBoardHeader,
            newItem: GameBoardHeader
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}