package ru.andreewkov.weightdrop.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

typealias SignalFlow = SharedFlow<Signal>

typealias MutableSignalFlow = MutableSharedFlow<Signal>

fun MutableSignalFlow() : MutableSignalFlow {
    return MutableSharedFlow()
}

fun MutableSignalFlow.asSignalFlow(): SignalFlow {
    return asSharedFlow()
}

suspend fun MutableSignalFlow.signal() {
    emit(Signal())
}

class Signal
