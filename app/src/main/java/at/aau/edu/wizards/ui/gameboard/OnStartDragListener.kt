package at.aau.edu.wizards.ui.gameboard

import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardItemViewHolder

interface OnStartDragListener {
    fun onStartDrag(viewHolder: GameBoardItemViewHolder)
}
