package at.aau.edu.wizards.gameboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.databinding.FragmentGameboardBinding


class GameboardFragment : Fragment() {


    private var binding: FragmentGameboardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameboardBinding.inflate(inflater, container, false)

        this.binding = binding

        return binding.root    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun onDestroyView() {

    }

    private fun setupUI() {

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

