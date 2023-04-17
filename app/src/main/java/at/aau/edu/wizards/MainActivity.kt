package at.aau.edu.wizards

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import at.aau.edu.wizards.api.impl.REQUIRED_PERMISSIONS
import at.aau.edu.wizards.sample.SampleFragment
import at.aau.edu.wizards.scoreboard.ScoreboardFragment
import at.aau.edu.wizards.util.permission.PermissionHandler

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.sample_button)?.let { sampleButton ->
            sampleButton.setOnClickListener {
                showSampleFragment()
            }
        }

        findViewById<Button>(R.id.scoreboard_button)?.let { scoreboardButton ->
            scoreboardButton.setOnClickListener {
                showScoreboardFragment()
            }
        }

        handlePermissions()
    }

    private fun showSampleFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SampleFragment(), "TAG")
            .addToBackStack(null)
            .commit()
    }

    private fun showScoreboardFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ScoreboardFragment(), "TAG")
            .addToBackStack(null)
            .commit()
    }

    private fun handlePermissions() {
        val permissionHandler = PermissionHandler(
            activity = this,
            permissions = REQUIRED_PERMISSIONS
        ) { permissions ->
            val anyDenied = permissions.any { !it.value }

            println("Any permissions denied = $anyDenied")
            // TODO: Need to properly handle denied permissions. App won't work without
        }
        lifecycle.addObserver(permissionHandler)
    }
}