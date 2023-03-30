package at.aau.edu.wizards.gameModel

sealed class GameModelResult<out T> {

    data class Success<out R>(val output: R) : GameModelResult<R>()
    data class Failure(
            val throwable: Throwable
    ) : GameModelResult<Nothing>()

    inline fun <reified T> GameModelResult<T>.doIfSuccess(callback: (output: T) -> Unit) {
        if (this is GameModelResult.Success) {
            callback(output)
        }
    }

    inline fun <reified T> GameModelResult<T>.doIfFailure(callback: (throwable: Throwable) -> Unit) {
        if (this is GameModelResult.Failure) {
            callback(throwable)
        }
    }

    inline fun <reified T, reified R> GameModelResult<T>.map(transform: (T) -> R): GameModelResult<R> {
        return when (this) {
            is GameModelResult.Success -> GameModelResult.Success(transform(output))
            is GameModelResult.Failure -> this
        }
    }

    inline fun <T> GameModelResult<T>.withDefault(value: () -> T): GameModelResult.Success<T> {
        return when (this) {
            is GameModelResult.Success -> this
            is GameModelResult.Failure -> GameModelResult.Success(value())
        }
    }

}