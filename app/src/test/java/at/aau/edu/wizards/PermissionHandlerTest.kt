package at.aau.edu.wizards

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.testing.TestLifecycleOwner
import at.aau.edu.wizards.util.permission.PermissionChecker
import at.aau.edu.wizards.util.permission.PermissionHandler
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

internal class PermissionHandlerTest {

    private val acceptedPermission = "Permission 1"
    private val deniedPermission = "Permission 2"
    private val activity = mock<AppCompatActivity>()
    private val permissions = listOf(acceptedPermission, deniedPermission)
    private val deniedPermissions = mock<(Map<String, Boolean>) -> Unit>()
    private val permissionChecker = mock<PermissionChecker>()
    private val permissionHandler = PermissionHandler(
        activity = activity,
        permissions = permissions,
        permissionChecker = permissionChecker,
        deniedPermissions = deniedPermissions
    )

    private val lifecycleOwner = TestLifecycleOwner()
    private val resultLauncher = mock<ActivityResultLauncher<Array<String>>>()

    @Test
    fun `given permission handler, on lifecycle changes, assert permissions are requested`() {
        whenever(
            activity.registerForActivityResult(
                any<ActivityResultContracts.RequestMultiplePermissions>(), any()
            )
        ).doReturn(resultLauncher)

        permissionHandler.onCreate(lifecycleOwner)

        // That test is really not perfect, but I struggled to get proper matchers working
        // at least it's verifying that we register something :/
        then(activity).should().registerForActivityResult(
            any<ActivityResultContracts.RequestMultiplePermissions>(),
            any()
        )

        whenever(permissionChecker.isGranted(activity, acceptedPermission))
            .doReturn(true)

        whenever(permissionChecker.isGranted(activity, deniedPermission))
            .doReturn(false)

        permissionHandler.onResume(lifecycleOwner)

        then(permissionChecker).should().isGranted(activity, acceptedPermission)
        then(permissionChecker).should().isGranted(activity, deniedPermission)
        // only ask denied permissions
        then(resultLauncher).should().launch(arrayOf(deniedPermission))
    }
}