package at.aau.edu.wizards.ui.gameboard.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.ui.gameboard.Cards

class GameBoardAdapter : ListAdapter<Cards, GameBoardItemViewHolder>(DiffUtlCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameBoardItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(from, parent, false)
        return GameBoardItemViewHolder(binding)
    }

    /*
    override fun onBindViewHolder(holder: GameBoardItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

     */

    override fun onBindViewHolder(holder: GameBoardItemViewHolder, position: Int) {
        val card = getItem(position)
        holder.binding.cardImageView.setImageResource(card.cardImage)
        // Set the drag event listener for the item view
        holder.binding.cardImageView.setOnDragListener { v, event ->
            if (event.action == DragEvent.ACTION_DRAG_STARTED) {
                v.visibility = View.INVISIBLE
                return@setOnDragListener true
            } else if (event.action == DragEvent.ACTION_DRAG_ENDED) {
                v.visibility = View.VISIBLE
                return@setOnDragListener true
            }
            false
        }
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