package ru.andreewkov.weightdrop.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainProviderModule {

    @Provides
    @Singleton
    @HistoryDispatcherQualifier
    fun provideHistoryDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }
}
