package ru.andreewkov.weightdrop.database

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DatabaseDispatcherProvider {
    fun get(): CoroutineDispatcher
}

internal class DatabaseDispatcherProviderImpl : DatabaseDispatcherProvider {
    override fun get() = Dispatchers.IO
}