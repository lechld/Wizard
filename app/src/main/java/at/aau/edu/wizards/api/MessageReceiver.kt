package at.aau.edu.wizards.api

import at.aau.edu.wizards.api.model.Data
import kotlinx.coroutines.flow.Flow

val EMPTY_DATA = Data("", "")

interface MessageReceiver {
    val messages: Flow<Data>
}