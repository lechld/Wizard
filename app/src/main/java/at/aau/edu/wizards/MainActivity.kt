package at.aau.edu.wizards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import at.aau.edu.wizards.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*findViewById<Button>(R.id.sample_button)?.let { sampleButton ->
            sampleButton.setOnClickListener {
                showSampleFragment()
            }
        } */

        populateCards()

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(applicationContext, 1,
            LinearLayoutManager.HORIZONTAL, true)

            adapter = CardAdapter(cardList)
        }
    }

    private fun populateCards() {

        val card1 = Cards(
            R.drawable.card
        )
        cardList.add(card1)

        val card2 = Cards(
            R.drawable.card
        )
        cardList.add(card2)

        val card3 = Cards(
            R.drawable.card
        )
        cardList.add(card3)

        val card4 = Cards(
            R.drawable.card
        )
        cardList.add(card4)

        val card5 = Cards(
            R.drawable.card
        )
        cardList.add(card5)

        val card6 = Cards(
            R.drawable.card
        )
        cardList.add(card6)

        val card7 = Cards(
            R.drawable.card
        )
        cardList.add(card7)

        val card8 = Cards(
            R.drawable.card
        )
        cardList.add(card8)

        val card9 = Cards(
            R.drawable.card
        )
        cardList.add(card9)

        val card10 = Cards(
            R.drawable.card
        )
        cardList.add(card10)
    }


    /*private fun showSampleFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SampleFragment(), "TAG")
            .commit()
    } */
}