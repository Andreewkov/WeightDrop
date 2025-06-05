package ru.andreewkov.weightdrop.data.impl

import ru.andreewkov.weightdrop.utils.api.Logger

internal fun <T> T.asSuccess(): Result<T> {
    return Result.success(this)
}

internal fun <T> Throwable.asFailure(): Result<T> {
    return Result.failure(this)
}

internal fun <T> Result<T>.logIfFailure(logger: Logger, message: String): Result<T> {
    exceptionOrNull()?.let { e ->
        logger.e(message, e)
    }
    return this
}

internal fun <T> runCatching(
    logger: Logger,
    errorMessage: String,
    action: () -> T,
): Result<T> {
    return try {
        Result.success(action())
    } catch (e: Throwable) {
        logger.e(errorMessage, e)
        Result.failure(e)
    }
}
