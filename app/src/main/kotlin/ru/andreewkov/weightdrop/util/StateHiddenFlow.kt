package ru.andreewkov.weightdrop.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateHiddenFlow<T>(initialValue: T) {

    private val flow = MutableStateFlow(initialValue)

    val value get() = flow.value

    fun get(): StateFlow<T> {
        return flow.asStateFlow()
    }

    fun update(value: T) {
        flow.value = value
    }
}