package at.aau.edu.wizards.ui.popup.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemPopupBinding
import at.aau.edu.wizards.ui.popup.PopUp

class PopUpAdapter : ListAdapter<PopUp, PopUpItemViewHolder>(DiffUtlCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopUpItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemPopupBinding.inflate(from, parent, false)
        return PopUpItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopUpItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffUtlCallback : DiffUtil.ItemCallback<PopUp>() {
        override fun areItemsTheSame(oldItem: PopUp, newItem: PopUp): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PopUp, newItem: PopUp): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}