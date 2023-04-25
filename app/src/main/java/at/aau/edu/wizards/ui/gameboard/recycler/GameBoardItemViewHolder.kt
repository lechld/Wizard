package at.aau.edu.wizards.ui.gameboard.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.ui.gameboard.Cards

class GameBoardItemViewHolder(
    private val binding: ItemCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Cards) {
        binding.cardImage.setImageResource(item.cardImage)
    }
}