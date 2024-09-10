package eu.anifantakis.project.library.masterdetailmodern.core.domain.util

sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E: Error>(val error: E) : Result<Nothing, E>

}

inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Failure -> Result.Failure(error)
    }
}

typealias EmptyDataResult<E> = Result<Unit, E>

fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyDataResult<E> {
    return map { }
}