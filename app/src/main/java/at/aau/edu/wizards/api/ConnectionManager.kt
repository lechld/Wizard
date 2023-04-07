package at.aau.edu.wizards.api

import android.content.Context
import at.aau.edu.wizards.api.impl.ConnectionManagerImpl
import at.aau.edu.wizards.api.model.Data
import com.google.android.gms.nearby.Nearby
import kotlinx.coroutines.flow.Flow

interface ConnectionManager {
    val messages: Flow<Data?>
    val client: Client
    val server: Server

    fun send(endpointId: String, data: String)

    companion object {
        private var instance: ConnectionManager? = null

        fun getInstance(context: Context): ConnectionManager {
            val instance = this.instance

            if (instance != null) {
                return instance
            }

            val newInstance = ConnectionManagerImpl(
                connectionsClient = Nearby.getConnectionsClient(context)
            )

            this.instance = newInstance
            return newInstance
        }
    }
}