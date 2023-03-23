package at.aau.edu.wizards.sample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SampleDataSourceImpl: SampleDataSource {

    override suspend fun provideData(
        input: String
    ): List<String> = withContext(Dispatchers.IO) { // withContext switches to IO thread here (not on main!)
        delay(1000L) // fake some loading time

        return@withContext listOf("first value", "second value", "third value")
    }
}