package at.aau.edu.wizards.util.permission

import android.content.Context

interface PermissionChecker {
    fun isGranted(context: Context, permission: String): Boolean
}