package at.aau.edu.wizards.ui.discover

import androidx.lifecycle.*
import at.aau.edu.wizards.*
import at.aau.edu.wizards.api.Client
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class DiscoverViewModel(
    private val client: Client,
    private val discoverItemFactory: DiscoverItemFactory = DiscoverItemFactory()
) : ViewModel() {

    val items: LiveData<List<DiscoverItem>> = client.connections.map { connections ->
        discoverItemFactory.create(connections)
    }.asLiveData()

    fun startDiscovery() {
        client.startDiscovery()
    }

    fun stopDiscovery() {
        client.stopDiscovery()
    }

    fun connectEndpoint(discoverItem: DiscoverItem.Pending) {
        client.connect(discoverItem.connection)
    }

    class Factory(
        private val client: Client
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(DiscoverViewModel::class.java)) {
                DiscoverViewModel(client) as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}