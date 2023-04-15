package at.aau.edu.wizards.draganddrop

interface DragAndDrop_DataSource {
    // suspend just says it needs to be called from a coroutine
    // all u need to know is that this allows us threading
    suspend fun provideData(input: String): List<String>
}