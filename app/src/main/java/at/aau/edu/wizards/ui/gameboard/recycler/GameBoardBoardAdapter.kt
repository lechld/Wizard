package at.aau.edu.wizards.ui.gameboard.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.gameModel.GameModel
import at.aau.edu.wizards.gameModel.GameModelCard
import at.aau.edu.wizards.ui.gameboard.GameBoardViewModel

class GameBoardBoardAdapter(val viewModel: GameBoardViewModel, val model: GameModel) :
    ListAdapter<GameModelCard, GameBoardBoardItemViewHolder>(DiffUtilCallback(model)) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameBoardBoardItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(from, parent, false)
        return GameBoardBoardItemViewHolder(binding, model)
    }

    override fun onBindViewHolder(holder: GameBoardBoardItemViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                if (model.listener.guessing) {
                    viewModel.sendMessage(
                        (60 + model.listener.localPlayer() + holder.bindingAdapterPosition).toChar()
                            .toString()
                    )
                }
            }
        }
    }

    private class DiffUtilCallback(val model: GameModel) : DiffUtil.ItemCallback<GameModelCard>() {
        override fun areItemsTheSame(oldItem: GameModelCard, newItem: GameModelCard): Boolean {
            return oldItem.image() == newItem.image() && !model.listener.guessing
        }

        override fun areContentsTheSame(oldItem: GameModelCard, newItem: GameModelCard): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}