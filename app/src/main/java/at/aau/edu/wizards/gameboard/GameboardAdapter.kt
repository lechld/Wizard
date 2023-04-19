package at.aau.edu.wizards.gameboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.R
import com.google.android.material.imageview.ShapeableImageView

class GameboardAdapter(private val cardList: ArrayList<Cards>) : RecyclerView.Adapter<GameboardAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card,
            parent, false
        )

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = cardList[position]
        holder.titleImage.setImageResource(currentItem.cardsImage)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ShapeableImageView = itemView.findViewById(R.id.card_image)
    }

}