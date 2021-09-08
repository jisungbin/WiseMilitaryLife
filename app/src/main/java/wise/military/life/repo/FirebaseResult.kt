package wise.military.life.repo

sealed class FirebaseResult<out T> {
    data class Success<T>(val response: T) : FirebaseResult<T>()
    data class Fail(val exception: Exception) : FirebaseResult<Nothing>()
}

inline fun <T> FirebaseResult<T>.doWhen(
    onSuccess: (T) -> Unit,
    onFail: (exception: Exception) -> Unit = {},
) {
    when (this) {
        is FirebaseResult.Success -> onSuccess(response)
        is FirebaseResult.Fail -> onFail(exception)
    }
}
