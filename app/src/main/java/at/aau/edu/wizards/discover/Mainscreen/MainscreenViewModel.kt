package at.aau.edu.wizards.ui.discover

import androidx.lifecycle.*
import at.aau.edu.wizards.*
import at.aau.edu.wizards.api.Client
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.flow.*
import java.util.*

class MainscreenViewModel(
    private val client: Client,
    private val discoverItemFactory: MainscreenItemFactory = MainscreenItemFactory()
) : ViewModel() {

    val items: LiveData<List<MainscreenItem>> = client.connections.map { connections ->
        discoverItemFactory.create(connections)
    }.asLiveData()

    fun startDiscovery() {
        client.startDiscovery()
    }

    fun stopDiscovery() {
        client.stopDiscovery()
    }

    fun connectEndpoint(discoverItem: MainscreenItem.Pending) {
        client.connect(discoverItem.connection)
    }

    class Factory(
        private val client: Client
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(MainscreenViewModel::class.java)) {
                MainscreenViewModel(client) as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}