package at.aau.edu.wizards.ui.gameboard.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.ui.gameboard.Cards
import at.aau.edu.wizards.ui.gameboard.OnStartDragListener

class GameBoardAdapter(private val onStartDragListener: OnStartDragListener) :
    ListAdapter<Cards, GameBoardItemViewHolder>(DiffUtlCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameBoardItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(from, parent, false)
        return GameBoardItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: GameBoardItemViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.binding.cardImage.setOnLongClickListener {
            onStartDragListener.onStartDrag(holder)
            true
        }
    }
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback())
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private class DiffUtlCallback : DiffUtil.ItemCallback<Cards>() {
        override fun areItemsTheSame(oldItem: Cards, newItem: Cards): Boolean {
            return oldItem.cardImage == newItem.cardImage
        }

        override fun areContentsTheSame(oldItem: Cards, newItem: Cards): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
    private inner class ItemTouchHelperCallback : ItemTouchHelper.Callback() {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            moveItem(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }
    }

    internal fun moveItem(fromPosition: Int, toPosition: Int) {
        val item = currentList[fromPosition]
        val newList = currentList.toMutableList()
        newList.removeAt(fromPosition)
        newList.add(toPosition, item)
        submitList(newList)
    }
}