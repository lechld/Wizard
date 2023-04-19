package at.aau.edu.wizards.gameboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import at.aau.edu.wizards.R


class GameboardFragment : Fragment() {

    private lateinit var adapter: GameboardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardsArrayList: ArrayList<Cards>

    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gameboard, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()

        val layoutManager = GridLayoutManager(context, 1,
                LinearLayoutManager.HORIZONTAL, true)
        recyclerView = view.findViewById(R.id.gameboard_recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = GameboardAdapter(cardsArrayList)
        recyclerView.adapter = adapter
    }

    private fun dataInitialize() {

        cardsArrayList = arrayListOf<Cards>()

        imageId = arrayOf(
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card
        )


        for (i in imageId.indices){
            val news = Cards(imageId[i])
            cardsArrayList.add(news)
        }
    }
}

