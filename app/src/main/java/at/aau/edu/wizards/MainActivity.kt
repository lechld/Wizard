package at.aau.edu.wizards

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import at.aau.edu.wizards.sample.SampleFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.sample_button)?.let { sampleButton ->
            sampleButton.setOnClickListener {
                showSampleFragment()
            }
        }
    }

    private fun showSampleFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SampleFragment(), "TAG")
            .commit()
    }
}