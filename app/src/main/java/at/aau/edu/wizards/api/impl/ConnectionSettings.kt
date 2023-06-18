package at.aau.edu.wizards.api.impl

import android.os.Build
import at.aau.edu.wizards.USERDATA_AVATAR
import at.aau.edu.wizards.USERDATA_USERNAME
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.Strategy
import java.text.SimpleDateFormat
import java.util.*

private val strategy = Strategy.P2P_STAR

val advertisingOptions = AdvertisingOptions.Builder()
    .setStrategy(strategy)
    .build()

val discoveryOptions = DiscoveryOptions.Builder()
    .setStrategy(strategy)
    .build()

val REQUIRED_PERMISSIONS: List<String> = mutableListOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
).apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        add(android.Manifest.permission.BLUETOOTH_ADVERTISE)
        add(android.Manifest.permission.BLUETOOTH_CONNECT)
        add(android.Manifest.permission.BLUETOOTH_SCAN)
    }
}