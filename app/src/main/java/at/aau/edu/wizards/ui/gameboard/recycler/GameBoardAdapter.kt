package at.aau.edu.wizards.ui.gameboard.recycler

import android.R.attr.shape
import android.view.LayoutInflater
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.gameModel.GameModelCard
class GameBoardAdapter(
    private val onClick: (GameModelCard) -> Unit,
) : ListAdapter<GameModelCard, GameBoardItemViewHolder>(DiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameBoardItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(from, parent, false)

        return GameBoardItemViewHolder(binding).apply {
            /*binding.root.setOnLongClickListener {
                val item = getItem(this.bindingAdapterPosition)
                val shadow = DragShadowBuilder(binding.root)
                ViewCompat.startDragAndDrop(binding.root, null, shadow, item, 0)
                true
            } */
        }
    }

    override fun onBindViewHolder(holder: GameBoardItemViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.bind(getItem(position)).apply {
            holder.itemView.setOnLongClickListener {
                val item = getItem(holder.bindingAdapterPosition)
                val shadow = DragShadowBuilder(holder.itemView)
                ViewCompat.startDragAndDrop(holder.itemView, null, shadow, item, 0)
                onClick(item)
                true
            }
        }

       /* holder.itemView.setOnClickListener {

            val item = getItem(holder.bindingAdapterPosition)
            onClick(item)
        }

        */
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