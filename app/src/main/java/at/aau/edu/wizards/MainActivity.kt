package at.aau.edu.wizards

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import at.aau.edu.wizards.sample.SampleFragment
import at.aau.edu.wizards.api.impl.REQUIRED_PERMISSIONS
import at.aau.edu.wizards.databinding.ActivityMainBinding
import at.aau.edu.wizards.gameboard.GameboardFragment
import at.aau.edu.wizards.util.permission.PermissionHandler

private const val GAMEBOARD_FRAGMENT_TAG = "GAMEBOARD_FRAGMENT_TAG"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        findViewById<Button>(R.id.sample_button)?.let { sampleButton ->
            sampleButton.setOnClickListener {
                showSampleFragment()
            }
        }

        setupUi()
        handlePermissions()
    }

    private fun setupUi() {
        binding.gamebutton.setOnClickListener {
            showGame()
        }
    }

    private fun showGame() {
        val fragment = supportFragmentManager.findFragmentByTag(GAMEBOARD_FRAGMENT_TAG)
            ?: GameboardFragment()

        if (fragment.isAdded) {
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, GAMEBOARD_FRAGMENT_TAG)
            .addToBackStack(GAMEBOARD_FRAGMENT_TAG)
            .commit()
    }


    private fun showSampleFragment() {
      supportFragmentManager.beginTransaction()
          .add(R.id.fragment_container, SampleFragment(), "TAG")
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