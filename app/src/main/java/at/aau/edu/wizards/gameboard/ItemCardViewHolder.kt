package at.aau.edu.wizards.gameboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.R
import com.google.android.material.imageview.ShapeableImageView

class ItemCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleImage: ShapeableImageView = itemView.findViewById(R.id.card_image)
}