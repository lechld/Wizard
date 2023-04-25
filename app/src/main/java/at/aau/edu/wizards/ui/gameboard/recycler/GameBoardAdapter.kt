package at.aau.edu.wizards.ui.gameboard.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.CardBinding
import at.aau.edu.wizards.ui.gameboard.Cards

class GameBoardAdapter : ListAdapter<Cards, ItemCardViewHolder>(DiffUtlCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardBinding.inflate(from, parent, false)
        return ItemCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemCardViewHolder, position: Int) {
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