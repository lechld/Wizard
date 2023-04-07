package at.aau.edu.wizards.api.model

sealed interface Connection {
    val endpointId: String

    data class Pending(override val endpointId: String) : Connection
    data class Success(override val endpointId: String) : Connection
    data class Failure(override val endpointId: String, val cause: Throwable) : Connection
}