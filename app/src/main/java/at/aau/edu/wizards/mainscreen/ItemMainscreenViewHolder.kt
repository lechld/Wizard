package at.aau.edu.wizards.mainscreen

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemMainscreenBinding

class ItemMainscreenViewHolder (private val binding: ItemMainscreenBinding, // Yeah, also for your item a binding class is generated
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Room) {
        // here you fill the item_xxx.xml with the data of the user. Eg setting name into text view
        binding.listElement.text = item.name

    }
}