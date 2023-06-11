package at.aau.edu.wizards.ui.gameboard.claim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemGuessBinding

class GuessAdapter(
    private val onClick: (Int) -> Unit,
) : ListAdapter<Int, GuessViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuessViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemGuessBinding.inflate(from, parent, false)

        return GuessViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: GuessViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    private class DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
}