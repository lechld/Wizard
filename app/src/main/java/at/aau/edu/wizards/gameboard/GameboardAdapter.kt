package at.aau.edu.wizards.gameboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.CardBinding


class GameboardAdapter(private val cardList: ArrayList<Cards>) :
        ListAdapter<Cards, ItemCardViewHolder>(DiffUtlCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardBinding.inflate(from, parent, false)
        return ItemCardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: ItemCardViewHolder, position: Int) {
        holder.bindCard(cardList[position])
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