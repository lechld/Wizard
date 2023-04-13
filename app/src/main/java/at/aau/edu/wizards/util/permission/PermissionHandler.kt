package at.aau.edu.wizards.util.permission

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PermissionHandler(
    private val activity: ComponentActivity,
    private val permissions: List<String>,
    private val permissionChecker: PermissionChecker = PermissionCheckerImpl(),
    private val deniedPermissions: (Map<String, Boolean>) -> Unit,
) : DefaultLifecycleObserver {

    private var resultLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        resultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            deniedPermissions
        )
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        // Check permission every time we enter the lifecycle owner
        // user could have removed a specific permission inside settings
        val permissionsToRequest = permissions.filter { permission ->
            !permissionChecker.isGranted(activity, permission)
        }

        resultLauncher?.launch(permissionsToRequest.toTypedArray())
    }
}