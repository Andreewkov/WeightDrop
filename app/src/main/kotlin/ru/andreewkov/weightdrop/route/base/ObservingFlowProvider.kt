package ru.andreewkov.weightdrop.route.base

import kotlinx.coroutines.flow.Flow

interface ObservingFlowProvider<ProvidedType> {

    fun provideObservingFlow(): Flow<ProvidedType>
}
