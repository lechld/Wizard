package at.aau.edu.wizards.ui.gameboard.claim

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemGuessBinding

class GuessViewHolder(
    private val binding: ItemGuessBinding,
    private val onClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(value: Int) {
        binding.root.text = value.toString()
        binding.root.setOnClickListener {
            onClick(value)
        }
    }
}