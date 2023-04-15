package at.aau.edu.wizards.Mainscreen

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MainscreenDataSourceImpl: Mainscreen_DataSource {

    override suspend fun provideData(
        input: String
    ): List<String> = withContext(Dispatchers.IO) { // withContext switches to IO thread here (not on main!)
        delay(1000L) // fake some loading time

        return@withContext listOf("first value", "second value", "third value")
    }
}