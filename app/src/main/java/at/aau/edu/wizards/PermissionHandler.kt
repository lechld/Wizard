package at.aau.edu.wizards

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PermissionHandler(
    private val activity: ComponentActivity,
    private val deniedPermissions: (Boolean) -> Unit,
) : DefaultLifecycleObserver {

    private val permissions = mutableListOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(android.Manifest.permission.BLUETOOTH_ADVERTISE)
            add(android.Manifest.permission.BLUETOOTH_CONNECT)
            add(android.Manifest.permission.BLUETOOTH_SCAN)
        }
    }

    private var resultLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        resultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val anyDenied = permissions.any { !it.value }

            deniedPermissions(anyDenied)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        // Check permission every time we enter the lifecycle owner
        // user could have removed a specific permission inside settings
        val permissionsToRequest = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }

        resultLauncher?.launch(permissionsToRequest.toTypedArray())
    }
}