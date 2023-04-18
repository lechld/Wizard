package at.aau.edu.wizards.api

import at.aau.edu.wizards.api.model.Connection

interface MessageSender {
    fun send(connection: Connection, data: String)
}