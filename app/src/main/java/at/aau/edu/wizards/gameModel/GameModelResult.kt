package at.aau.edu.wizards.gameModel

sealed class GameModelResult<out T> {

    data class Success<out R>(val output: R) : GameModelResult<R>()
    data class Failure(
        val throwable: Throwable
    ) : GameModelResult<Nothing>()

}