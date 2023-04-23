package at.aau.edu.wizards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.aau.edu.wizards.api.impl.REQUIRED_PERMISSIONS
import at.aau.edu.wizards.databinding.ActivityMainBinding
import at.aau.edu.wizards.ui.discover.MainscreenFragment
import at.aau.edu.wizards.util.permission.PermissionHandler

private const val DISCOVER_FRAGMENT_TAG = "DISCOVER_FRAGMENT_TAG"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupUi()
        handlePermissions()
    }

    private fun setupUi() {
        binding.clientButton.setOnClickListener {
            showDiscoverFragment()
        }

        binding.serverButton.setOnClickListener {
            showLobby()
        }

        binding.gameButton.setOnClickListener {
            showGame()
        }
    }

    private fun showDiscoverFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(DISCOVER_FRAGMENT_TAG)
            ?: MainscreenFragment()

        if (fragment.isAdded) {
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, DISCOVER_FRAGMENT_TAG)
            .addToBackStack(DISCOVER_FRAGMENT_TAG)
            .commit()
    }

    private fun showLobby() {
        // TODO: Ena, copy paste from showDiscover and adjust properly
    }

    private fun showGame() {
        // TODO: Marting copy paste from showDiscover and adjust properly
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