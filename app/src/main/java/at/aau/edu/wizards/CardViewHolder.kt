package at.aau.edu.wizards


import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.CardBinding

class CardViewHolder(
    private val cardCellBinding: CardBinding
) : RecyclerView.ViewHolder(cardCellBinding.root)
{
    fun bindCard(cards: Cards)
    {
        cardCellBinding.card.setImageResource(cards.card)
    }


}