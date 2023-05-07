package at.aau.edu.wizards

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import at.aau.edu.wizards.api.impl.REQUIRED_PERMISSIONS
import at.aau.edu.wizards.databinding.ActivityMainBinding
import at.aau.edu.wizards.ui.gameboard.GameBoardFragment
import at.aau.edu.wizards.ui.discover.DiscoverFragment
import at.aau.edu.wizards.ui.lobby.LobbyFragment
import at.aau.edu.wizards.util.permission.PermissionHandler
import com.google.android.material.snackbar.Snackbar

private const val DISCOVER_FRAGMENT_TAG = "DISCOVER_FRAGMENT_TAG"
private const val LOBBY_FRAGMENT_TAG = "LOBBY_FRAGMENT_TAG"
private const val GAME_BOARD_FRAGMENT_TAG = "GAME_BOARD_FRAGMENT_TAG"
private const val SHARED_PREFERENCE_USERNAME_KEY = "USERNAME"
private const val SHARED_PREFERENCE_NAME = "SHARED_DATA"
public const val AUTOFILL_HINT_USERNAME = "username"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupUi()
        handlePermissions()
    }

    fun showGame(asClient: Boolean, amountCpu: Int = 0) {
        val fragment = supportFragmentManager.findFragmentByTag(GAME_BOARD_FRAGMENT_TAG)
            ?: GameBoardFragment.instance(asClient, amountCpu)

        if (fragment.isAdded) {
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, GAME_BOARD_FRAGMENT_TAG)
            .addToBackStack(GAME_BOARD_FRAGMENT_TAG)
            .commit()
    }

    private fun setupUi() {
        val sharedPrefs: SharedPreferences = getSharedPreferences(getString(R.string.shared_storage), Context.MODE_PRIVATE)
        if (usernameAlreadyExists(sharedPrefs)) {
            binding.inputUsername.setText(sharedPrefs.getString(SHARED_PREFERENCE_USERNAME_KEY, ""))
            setButtonsEnableStatus(true)
        }
        else {
            setButtonsEnableStatus(false)
        }

        binding.inputUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Not required
            }
            override fun onTextChanged(typedText: CharSequence, i: Int, i1: Int, i2: Int) {
                if (typedText.isNotEmpty()) {
                    setButtonsEnableStatus(true)
                }
                else {
                    setButtonsEnableStatus(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {
                // Not required
            }
        })
        binding.clientButton.setOnClickListener {
            if (binding.clientButton.isEnabled) {
                showDiscoverFragment()
            }
        }

        binding.serverButton.setOnClickListener {
            if (binding.serverButton.isEnabled) {
                showLobby()
            }
        }
    }

    private fun usernameAlreadyExists(sharedPrefs: SharedPreferences): Boolean {
        return sharedPrefs.getString(SHARED_PREFERENCE_USERNAME_KEY, null) != null
    }

    private fun setButtonsEnableStatus(enabled: Boolean) {
        binding.clientButton.isEnabled = enabled
        binding.serverButton.isEnabled = enabled
    }

    private fun showDiscoverFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(DISCOVER_FRAGMENT_TAG)
            ?: DiscoverFragment()

        if (fragment.isAdded) {
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, DISCOVER_FRAGMENT_TAG)
            .addToBackStack(DISCOVER_FRAGMENT_TAG)
            .commit()
    }

    private fun showLobby() {
        val fragment = supportFragmentManager.findFragmentByTag(LOBBY_FRAGMENT_TAG)
            ?: LobbyFragment()

        if (fragment.isAdded) {
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, LOBBY_FRAGMENT_TAG)
            .addToBackStack(LOBBY_FRAGMENT_TAG)
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