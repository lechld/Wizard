package at.aau.edu.wizards.ui.gameboard.recycler

import android.R.attr.shape
import android.view.LayoutInflater
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.ui.gameboard.Cards


class GameBoardAdapter : ListAdapter<Cards, GameBoardItemViewHolder>(DiffUtlCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameBoardItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(from, parent, false)

        return GameBoardItemViewHolder(binding).apply {
            binding.root.setOnLongClickListener {
                val item = getItem(this.bindingAdapterPosition)
                val shadow = DragShadowBuilder(binding.root)
                ViewCompat.startDragAndDrop(binding.root, null, shadow, item, 0)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: GameBoardItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffUtlCallback : DiffUtil.ItemCallback<Cards>() {
        override fun areItemsTheSame(oldItem: Cards, newItem: Cards): Boolean {
            return oldItem.cardImage == newItem.cardImage
        }

        override fun areContentsTheSame(oldItem: Cards, newItem: Cards): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}