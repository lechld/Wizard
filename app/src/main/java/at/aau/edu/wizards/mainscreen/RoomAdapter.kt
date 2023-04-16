package at.aau.edu.wizards.mainscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemMainscreenBinding

class RoomAdapter: ListAdapter<Room, ItemMainscreenViewHolder>(RoomDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMainscreenViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemMainscreenBinding.inflate(inflater, parent, false) // Init your binding

        return ItemMainscreenViewHolder(binding) // and create a new viewholder instance of the viewholder we cerated before
    }

    override fun onBindViewHolder(holder: ItemMainscreenViewHolder, position: Int) {
        val item = getItem(position) // item is now a User

        holder.bind(item) // that will set the data inside the view holder class to the layout
    }
}

