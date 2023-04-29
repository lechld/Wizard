package at.aau.edu.wizards.ui.gameboard.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.gameModel.GameModelCard
import at.aau.edu.wizards.ui.gameboard.GameBoardViewModel

class GameBoardAdapter(val viewModel: GameBoardViewModel) :
    ListAdapter<GameModelCard, GameBoardItemViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameBoardItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(from, parent, false)
        return GameBoardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameBoardItemViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION && viewModel.cards.value?.get(
                    holder.bindingAdapterPosition
                ) != null
            ) {
                viewModel.sendMessage(viewModel.cards.value!![holder.bindingAdapterPosition].getString()) //probably solved pretty poorly, just want to get it running for now though
            }
        }
    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<GameModelCard>() {
        override fun areItemsTheSame(oldItem: GameModelCard, newItem: GameModelCard): Boolean {
            return oldItem.image() == newItem.image()
        }

        override fun areContentsTheSame(oldItem: GameModelCard, newItem: GameModelCard): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}