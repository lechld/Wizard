package at.aau.edu.wizards.ui.popup.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemPopupBinding
import at.aau.edu.wizards.ui.popup.PopUp

class PopUpItemViewHolder(
    private val binding: ItemPopupBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PopUp) {
        binding.avatarImage.setImageResource(item.winningCard)
    }
}