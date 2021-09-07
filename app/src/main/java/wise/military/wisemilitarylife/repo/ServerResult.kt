package wise.military.wisemilitarylife.repo

sealed class ServerResult<out T> {
    data class Success<T>(val response: T) : ServerResult<T>()
    data class Fail(val exception: Exception) : ServerResult<Nothing>()
}

inline fun <T> ServerResult<T>.doWhen(
    onSuccess: (T) -> Unit,
    onFail: (exception: Exception) -> Unit = {},
) {
    when (this) {
        is ServerResult.Success -> onSuccess(response)
        is ServerResult.Fail -> onFail(exception)
    }
}
