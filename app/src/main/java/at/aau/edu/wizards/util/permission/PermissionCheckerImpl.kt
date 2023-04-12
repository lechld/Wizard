package at.aau.edu.wizards.util.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionCheckerImpl : PermissionChecker {

    override fun isGranted(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}