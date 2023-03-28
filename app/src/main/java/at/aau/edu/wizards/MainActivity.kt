package at.aau.edu.wizards

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import at.aau.edu.wizards.sample.SampleFragment

class MainActivity : AppCompatActivity() {

    private val permissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.BLUETOOTH_ADVERTISE,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.BLUETOOTH_SCAN,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.sample_button)?.let { sampleButton ->
            sampleButton.setOnClickListener {
                showSampleFragment()
            }
        }

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            println(permissions)
        }


        val ungranted = permissions.mapNotNull { requiredPermission ->
            val requestResult = ContextCompat.checkSelfPermission(this, requiredPermission)

            if (requestResult == PackageManager.PERMISSION_GRANTED) {
                return@mapNotNull null
            }

            return@mapNotNull requiredPermission
        }

        permissionLauncher.launch(ungranted.toTypedArray())
    }

    private fun showSampleFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SampleFragment(), "TAG")
            .commit()
    }
}