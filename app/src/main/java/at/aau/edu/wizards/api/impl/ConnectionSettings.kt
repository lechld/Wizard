package at.aau.edu.wizards.api.impl

import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.Strategy
import java.text.DateFormat
import java.util.*

private val strategy = Strategy.P2P_STAR

val advertisingOptions = AdvertisingOptions.Builder()
    .setStrategy(strategy)
    .build()

val discoveryOptions = DiscoveryOptions.Builder()
    .setStrategy(strategy)
    .build()

// TODO: Need to provide some functionality to pick a name somewhere
val GENERATED_NAME: String = "GEN" + DateFormat.getDateInstance().format(Date())