package at.aau.edu.wizards.ui.gameboard.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.gameModel.GameModelCard

class GameBoardBoardAdapter : ListAdapter<GameModelCard, GameBoardItemViewHolder>(
    DiffUtilCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameBoardItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(from, parent, false)
        return GameBoardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameBoardItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<GameModelCard>() {
        override fun areItemsTheSame(oldItem: GameModelCard, newItem: GameModelCard): Boolean {
            return oldItem.image() == newItem.image()
        }

        override fun areContentsTheSame(oldItem: GameModelCard, newItem: GameModelCard): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}