package at.aau.edu.wizards

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.databinding.ActivityMainBinding
import at.aau.edu.wizards.gameModel.GameModelListener
import at.aau.edu.wizards.ui.discover.DiscoverFragment
import at.aau.edu.wizards.ui.gameboard.GameBoardFragment
import at.aau.edu.wizards.ui.lobby.LobbyFragment
import at.aau.edu.wizards.ui.scoreboard.ScoreboardFragment
import com.google.android.material.snackbar.Snackbar

private const val DISCOVER_FRAGMENT_TAG = "DISCOVER_FRAGMENT_TAG"
private const val LOBBY_FRAGMENT_TAG = "LOBBY_FRAGMENT_TAG"
private const val GAME_BOARD_FRAGMENT_TAG = "GAME_BOARD_FRAGMENT_TAG"
private const val SCOREBOARD_FRAGMENT_TAG = "SCOREBOARD_FRAGMENT_TAG"

private const val SHARED_PREFERENCES_KEY = "SHARED_PREFS_USERDATA"

class MainActivity : AppCompatActivity() {

    val FINE_LOCATION = 144

    var checkPermission = false
        private set

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // kotlin:S6291 ---> Make sure using an unencrypted database is safe here.
        @SuppressWarnings("kotlin:S6291")
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

        mainViewModel = ViewModelProvider(
            this,
            MainViewModel.Factory(sharedPreferences)
        )[MainViewModel::class.java]

        setContentView(binding.root)

        binding.settingsButton.visibility = View.INVISIBLE

        setupUi()
        setupSettings()
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
            }

            override fun afterTextChanged(p0: Editable?) {
                // Not used
            }
        })

        binding.clientButton.setOnClickListener {
            if (checkPermission) {
                if (!binding.inputUsername.text.toString().isNullOrEmpty()) {
                    showDiscoverFragment()
                } else {
                    Snackbar.make(
                        binding.root,
                        "Please enter username first!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                recheckPermissions(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    "Location",
                    FINE_LOCATION
                )
            }
        }

        binding.serverButton.setOnClickListener {
            if (checkPermission) {
                if (!binding.inputUsername.text.toString().isNullOrEmpty()) {
                    showLobby()
                } else {
                    Snackbar.make(
                        binding.root,
                        "Please enter username first!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                recheckPermissions(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    "Location",
                    FINE_LOCATION
                )
            }
        }
    }

    private fun setupSettings() {
        binding.settingsButton.setOnClickListener {
            val appSettings = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(
                    "package:$packageName"
                )
            )
            appSettings.addCategory(Intent.CATEGORY_DEFAULT)
            appSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(appSettings)
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

    private fun recheckPermissions(permission: String, name: String, requestCode: Int) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) -> {
                checkPermission = true
            }
            else -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) {
            checkPermission = if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                binding.settingsButton.visibility = View.VISIBLE
                Snackbar.make(
                    binding.root,
                    "$name permission refused - change location settings!",
                    Snackbar.LENGTH_LONG
                ).show()
                false
            } else {
                Snackbar.make(binding.root, "$name permission granted", Snackbar.LENGTH_LONG).show()
                binding.settingsButton.visibility = View.INVISIBLE
                true
            }
        }
        when (requestCode) {
            FINE_LOCATION -> innerCheck("Location")
        }
    }
}