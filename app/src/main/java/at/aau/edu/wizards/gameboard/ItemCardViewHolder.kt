package at.aau.edu.wizards.gameboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.CardBinding
import com.google.android.material.imageview.ShapeableImageView


class ItemCardViewHolder(
    private val cardCellBinding: CardBinding
) : RecyclerView.ViewHolder(cardCellBinding.root)
{
    fun bindCard(cards: Cards)
    {
        cardCellBinding.cardImage.setImageResource(cards.cardImage)
    }
}