package at.aau.edu.wizards

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.api.impl.REQUIRED_PERMISSIONS
import at.aau.edu.wizards.databinding.ActivityMainBinding
import at.aau.edu.wizards.gameModel.GameModelListener
import at.aau.edu.wizards.ui.discover.DiscoverFragment
import at.aau.edu.wizards.ui.gameboard.GameBoardFragment
import at.aau.edu.wizards.ui.lobby.LobbyFragment
import at.aau.edu.wizards.ui.scoreboard.ScoreboardFragment
import at.aau.edu.wizards.util.permission.PermissionHandler
import com.google.android.material.snackbar.Snackbar
import java.util.*

private const val DISCOVER_FRAGMENT_TAG = "DISCOVER_FRAGMENT_TAG"
private const val LOBBY_FRAGMENT_TAG = "LOBBY_FRAGMENT_TAG"
private const val GAME_BOARD_FRAGMENT_TAG = "GAME_BOARD_FRAGMENT_TAG"
private const val SCOREBOARD_FRAGMENT_TAG = "SCOREBOARD_FRAGMENT_TAG"
private const val SHARED_PREFERENCES_KEY = "SHARED_PREFS_USERDATA"

public const val KEY_USERNAME = "PLAYER_USERNAME"
public const val KEY_AVATAR = "PLAYER_AVATAR"

public var USERDATA_UUID: String = ""
public var USERDATA_USERNAME: String = ""
public var USERDATA_AVATAR: Int = R.drawable.icon1


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // kotlin:S6291 ---> Make sure using an unencrypted database is safe here.
        @SuppressWarnings("kotlin:S6291")
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

        mainViewModel = ViewModelProvider(this, MainViewModel.Factory(sharedPreferences))[MainViewModel::class.java]

        USERDATA_UUID = UUID.randomUUID().toString()
        USERDATA_USERNAME = mainViewModel.getUsername().toString()
        USERDATA_AVATAR = mainViewModel.getAvatar()

        setContentView(binding.root)

        setupAvatarSelection()
        setupUi()
        handlePermissions()
    }

    private fun setupAvatarSelection() {
        val adapter = ImageSpinnerAdapter(this, MainViewModel.avatarsList)
        binding.imageSpinner.adapter = adapter

        binding.imageView.setOnClickListener {
            binding.imageSpinner.performClick()
        }

        binding.imageSpinner.setSelection(MainViewModel.avatarsList.indexOf(mainViewModel.getAvatar()))

        binding.imageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val resourceId = MainViewModel.avatarsList[position]
                binding.imageView.setImageResource(resourceId)
                mainViewModel.saveAvatar(resourceId)
                USERDATA_AVATAR = mainViewModel.getAvatar()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
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

    fun showScoreboard(listener: GameModelListener) {
        val fragment = supportFragmentManager.findFragmentByTag(SCOREBOARD_FRAGMENT_TAG)
            ?: ScoreboardFragment(listener)

        if (fragment.isAdded) {
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, SCOREBOARD_FRAGMENT_TAG)
            .addToBackStack(SCOREBOARD_FRAGMENT_TAG)
            .commit()
    }

    private fun setupUi() {
        binding.inputUsername.setText(mainViewModel.getUsername())

        binding.inputUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Not used
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.saveUsername(binding.inputUsername.text.toString())
                USERDATA_USERNAME = mainViewModel.getUsername().toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // Not used
            }
        })

        binding.clientButton.setOnClickListener {
            if (!binding.inputUsername.text.toString().isNullOrEmpty()) {
                showDiscoverFragment()
            } else {
                Snackbar.make(binding.root, "Please enter username first!", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.serverButton.setOnClickListener {
            if (!binding.inputUsername.text.toString().isNullOrEmpty()) {
                showLobby()
            } else {
                Snackbar.make(binding.root, "Please enter username first!", Snackbar.LENGTH_LONG).show()
            }
        }
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

        val bundle = Bundle()
        bundle.putString(KEY_USERNAME, mainViewModel.getUsername())
        bundle.putInt(KEY_AVATAR, R.drawable.icon13)

        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, LOBBY_FRAGMENT_TAG)
            .addToBackStack(LOBBY_FRAGMENT_TAG)
            .commit()
    }

    fun scoreboardBack(finished: Boolean) {
        supportFragmentManager.popBackStack()
        if (finished) {
            supportFragmentManager.popBackStack()
        }
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