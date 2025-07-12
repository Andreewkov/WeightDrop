package ru.andreewkov.weightdrop.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

open class ShareHiddenFlow<T> {

    private val flow = MutableSharedFlow<T>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun get(): SharedFlow<T> {
        return flow.asSharedFlow()
    }

    fun update(value: T) {
        flow.tryEmit(value)
    }
}