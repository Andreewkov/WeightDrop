package ru.andreewkov.weightdrop.route.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class ObservingViewModel<State : ScreenState, ObservedType>(
    defaultState: State,
    private val flowProvider: ObservingFlowProvider<ObservedType>,
) : ScreenStateViewModel<State>(defaultState) {

    private var observeJob: Job

    abstract suspend fun handleObserved(value: ObservedType)

    abstract fun onFailureObserved(throwable: Throwable)

    init {
        observeJob = observe()
    }

    private fun observe(): Job {
        return flowProvider.provideObservingFlow().onEach { value ->
            runCatching {
                handleObserved(value)
            }.onFailure(::onFailureObserved)
        }.launchIn(viewModelScope)
    }

    protected fun retryObserve() {
        observeJob.cancel()
        observeJob = observe()
    }

    override fun onCleared() {
        observeJob.cancel()
        super.onCleared()
    }
}
