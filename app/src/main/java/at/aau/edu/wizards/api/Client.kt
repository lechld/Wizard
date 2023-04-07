package at.aau.edu.wizards.api

import android.content.Context
import at.aau.edu.wizards.api.impl.ClientImpl
import at.aau.edu.wizards.api.model.ClientConnection
import com.google.android.gms.nearby.Nearby
import kotlinx.coroutines.flow.Flow

interface Client : MessageSender, MessageReceiver {
    val connections: Flow<List<ClientConnection>>
    fun getConnections(): List<ClientConnection>

    fun startDiscovery()
    fun stopDiscovery()

    fun connect(connection: ClientConnection)

    companion object {
        private var instance: Client? = null

        fun getInstance(context: Context): Client {
            val instance = this.instance

            if (instance != null) {
                return instance
            }

            val newInstance = ClientImpl(
                connectionsClient = Nearby.getConnectionsClient(context)
            )

            this.instance = newInstance
            return newInstance
        }
    }
}