package ru.andreewkov.weightdrop.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

context(CoroutineScope)
fun <T> Flow<T>.observe(action: suspend (T) -> Unit) {
    onEach { action(it) }.launchIn(this@CoroutineScope)
}

fun <T, M> StateFlow<T>.map(
    coroutineScope : CoroutineScope,
    mapper : (value : T) -> M
) : StateFlow<M> {
    val flow = MutableStateFlow(
        mapper(value)
    )
    coroutineScope.launch {
        collect {
            flow.emit(mapper(it))
        }
    }
    return flow
}
