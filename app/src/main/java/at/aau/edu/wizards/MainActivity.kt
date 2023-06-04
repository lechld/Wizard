package at.aau.edu.wizards

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import at.aau.edu.wizards.databinding.ActivityMainBinding
import at.aau.edu.wizards.gameModel.GameModelListener
import at.aau.edu.wizards.ui.discover.DiscoverFragment
import at.aau.edu.wizards.ui.gameboard.GameBoardFragment
import at.aau.edu.wizards.ui.lobby.LobbyFragment
import at.aau.edu.wizards.ui.scoreboard.ScoreboardFragment


private const val DISCOVER_FRAGMENT_TAG = "DISCOVER_FRAGMENT_TAG"
private const val LOBBY_FRAGMENT_TAG = "LOBBY_FRAGMENT_TAG"
private const val GAME_BOARD_FRAGMENT_TAG = "GAME_BOARD_FRAGMENT_TAG"
private const val SCOREBOARD_FRAGMENT_TAG = "SCOREBOARD_FRAGMENT_TAG"

class MainActivity : AppCompatActivity() {

    val FINE_LOCATION = 144

    var checkPermission = false
        private set

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        //binding.permissionsButton.visibility = View.INVISIBLE
        setContentView(binding.root)

        setupUi()
        // handlePermissions()
        setupPermissions()
        if (!checkPermission) {
            recheckPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                "Location",
                FINE_LOCATION
            )
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
        binding.clientButton.setOnClickListener {
            showDiscoverFragment()
        }

        binding.serverButton.setOnClickListener {
            showLobby()
        }
    }

    private fun setupPermissions() {
        binding.permissionsButton.setOnClickListener {
            recheckPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                "Location",
                FINE_LOCATION
            )
        }
    }

    private fun showDiscoverFragment() {
        if (checkPermission) {
            val fragment = supportFragmentManager.findFragmentByTag(DISCOVER_FRAGMENT_TAG)
                ?: DiscoverFragment()

            if (fragment.isAdded) {
                return
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, DISCOVER_FRAGMENT_TAG)
                .addToBackStack(DISCOVER_FRAGMENT_TAG)
                .commit()
        } else {
            recheckPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                "Location",
                FINE_LOCATION
            )
        }
    }

    private fun showLobby() {
        if (checkPermission) {
            val fragment = supportFragmentManager.findFragmentByTag(LOBBY_FRAGMENT_TAG)
                ?: LobbyFragment()

            if (fragment.isAdded) {
                return
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, LOBBY_FRAGMENT_TAG)
                .addToBackStack(LOBBY_FRAGMENT_TAG)
                .commit()
        } else {
            recheckPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                "Location",
                FINE_LOCATION
            )
        }
    }

    fun scoreboardBack(finished: Boolean) {
        supportFragmentManager.popBackStack()
        if (finished) {
            supportFragmentManager.popBackStack()
        }
    }

    /* private fun handlePermissions() {
        val permissionHandler = PermissionHandler(
            activity = this,
            permissions = REQUIRED_PERMISSIONS
        ) { permissions ->
            val anyDenied = permissions.any { !it.value }

            if (anyDenied) {
                recheckPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", FINE_LOCATION)
            }
        }
        lifecycle.addObserver(permissionHandler)
    } */

    private fun recheckPermissions(permission: String, name: String, requestCode: Int) {
        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(
                    applicationContext,
                    "$name permission granted",
                    Toast.LENGTH_SHORT
                ).show()
                checkPermission = true
            }
            shouldShowRequestPermissionRationale(permission) -> showPermissionDialog(
                permission,
                name,
                requestCode
            )
            else -> {
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                checkPermission = false
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
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
                checkPermission = false
            } else {
                Toast.makeText(applicationContext, "$name permission grandted", Toast.LENGTH_SHORT)
                    .show()
                checkPermission = true
            }
        }
        when (requestCode) {
            FINE_LOCATION -> innerCheck("Location")
            else -> checkPermission = false
        }
    }

    private fun showPermissionDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}