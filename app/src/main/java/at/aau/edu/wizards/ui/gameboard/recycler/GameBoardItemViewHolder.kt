package at.aau.edu.wizards.ui.gameboard.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.gameModel.GameModelCard

class GameBoardItemViewHolder(
    private val binding: ItemCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GameModelCard) {
        binding.image.setImageResource(item.image())
        binding.text.text = item.getNumber()
    }
}