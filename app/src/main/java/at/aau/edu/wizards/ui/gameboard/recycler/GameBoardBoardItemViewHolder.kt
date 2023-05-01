package at.aau.edu.wizards.ui.gameboard.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.GameModelCard

class GameBoardBoardItemViewHolder(
    private val binding: ItemCardBinding,
    private val model: GameModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GameModelCard) {
        if (model.listener.guessing) {
            binding.cardImage.setImageResource(item.guessingImage())
            binding.root.setBackgroundResource(0)
        } else {
            binding.cardImage.setImageResource(item.image())
            binding.root.setBackgroundResource(
                R.color.md_theme_dark_background
            )
        }
    }
}