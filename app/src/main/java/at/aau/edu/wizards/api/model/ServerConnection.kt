package at.aau.edu.wizards.api.model

sealed interface ServerConnection: Connection {

    data class ClientRequest(
        override val endpointId: String,
        override val endpointName: String
    ) : ServerConnection

    data class Connected(
        override val endpointId: String,
        override val endpointName: String
    ) : ServerConnection

    data class Failure(
        override val endpointId: String,
        override val endpointName: String
    ) : ServerConnection
}