package at.aau.edu.wizards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.CardBinding

class CardAdapter(private val cards: List<Cards>)
    : RecyclerView.Adapter<CardViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardBinding.inflate(from, parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        holder.bindCard(cards[position])
    }
}