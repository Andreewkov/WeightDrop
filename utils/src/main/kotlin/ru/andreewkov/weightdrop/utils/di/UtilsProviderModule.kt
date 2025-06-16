package ru.andreewkov.weightdrop.utils.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.andreewkov.weightdrop.utils.api.Logger
import ru.andreewkov.weightdrop.utils.api.LoggerProvider
import ru.andreewkov.weightdrop.utils.impl.LoggerImpl

@Module
@InstallIn(SingletonComponent::class)
object UtilsProviderModule {

    @Provides
    fun provideLLoggerProvider(): LoggerProvider {
        return object : LoggerProvider {
            override fun get(tag: String): Logger {
                return LoggerImpl(tag)
            }
        }
    }
}
