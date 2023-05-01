package at.aau.edu.wizards.ui.gameboard.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.ui.gameboard.Cards
import at.aau.edu.wizards.ui.gameboard.OnStartDragListener

class GameBoardItemViewHolder(
    val binding: ItemCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Cards) {
        binding.cardImage.setImageResource(item.cardImage)
    }
}