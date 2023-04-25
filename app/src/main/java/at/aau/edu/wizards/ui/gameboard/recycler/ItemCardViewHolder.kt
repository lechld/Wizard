package at.aau.edu.wizards.ui.gameboard.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.CardBinding
import at.aau.edu.wizards.ui.gameboard.Cards

class ItemCardViewHolder(
    private val binding: CardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Cards) {
        binding.cardImage.setImageResource(item.cardImage)
    }
}