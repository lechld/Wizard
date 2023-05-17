package at.aau.edu.wizards.api

import android.content.Context
import at.aau.edu.wizards.api.impl.ServerImpl
import at.aau.edu.wizards.api.model.ServerConnection
import com.google.android.gms.nearby.Nearby
import kotlinx.coroutines.flow.Flow

interface Server : MessageSender, MessageReceiver {
    val connections: Flow<List<ServerConnection>>
    fun getConnectionsSync(): List<ServerConnection>

    fun startBroadcasting()
    fun stopBroadcasting()

    fun acceptClientRequest(connection: ServerConnection)
    fun declineClientRequest(connection: ServerConnection)

    companion object {
        private var instance: Server? = null

        fun getInstance(context: Context): Server {
            val instance = this.instance

            if (instance != null) {
                return instance
            }

            val newInstance = ServerImpl(
                connectionsClient = Nearby.getConnectionsClient(context)
            )

            this.instance = newInstance
            return newInstance
        }
    }
}

