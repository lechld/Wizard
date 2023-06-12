package at.aau.edu.wizards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

class ImageSpinnerAdapter(
    context: Context,
    private val imageIds: IntArray,
) : ArrayAdapter<String>(context, 0, Array(imageIds.size) { i -> "Avatar ${i + 1}" }) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_image_spinner,
            parent,
            false
        )

        val imageView: ImageView = view.findViewById(R.id.avatarImage)
        val imgResId = imageIds[position]
        imageView.setImageResource(imgResId)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
