package at.aau.edu.wizards.DiscoverTest

import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.ui.discover.DiscoverItemFactory
import at.aau.edu.wizards.ui.discover.DiscoverViewModel
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock


internal class DiscoverViewModelTest2 {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun getItems() {
    }


    @Test
    fun startDiscovery() {
        val client = Mockito.mock(Client::class.java)
        val viewModel = DiscoverViewModel(client, discoverItemFactory = DiscoverItemFactory())

    }

    @Test
    fun stopDiscovery() {
    }

    @Test
    fun connectEndpoint() {
    }
}