package at.aau.edu.wizards.mainscreen

import androidx.recyclerview.widget.DiffUtil

// inside the "< >" put your model class
class RoomDiffUtil:  DiffUtil.ItemCallback<Room>() {
    override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
        return oldItem == newItem
    }
}