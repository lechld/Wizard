package at.aau.edu.wizards

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import at.aau.edu.wizards.draganddrop.DragAndDropFragment
import at.aau.edu.wizards.mainscreen.MainscreenFragment
import at.aau.edu.wizards.sample.SampleFragment
import at.aau.edu.wizards.api.impl.REQUIRED_PERMISSIONS
import at.aau.edu.wizards.util.permission.PermissionHandler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.sample_button)?.let { sampleButton ->
            sampleButton.setOnClickListener {
                showSampleFragment()
            }
        }
        handlePermissions()

        findViewById<Button>(R.id.draganddrop_button)?.let { sampleButton ->
            sampleButton.setOnClickListener {
                showDragAndDrop()
            }
        }
        findViewById<Button>(R.id.mainscreen_button)?.let { sampleButton ->
            sampleButton.setOnClickListener {
                showMainscreen()
            }
        }
    }
    private fun showSampleFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SampleFragment(), "TAG")
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
    private fun showDragAndDrop(){
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, DragAndDropFragment(),"TAG")
            .commit()
    }
    private fun showMainscreen(){
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, MainscreenFragment(),"TAG")
            .commit()
    }
}