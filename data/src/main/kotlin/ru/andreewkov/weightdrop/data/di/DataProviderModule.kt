package ru.andreewkov.weightdrop.data.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

private const val SETTINGS_SHARED_PREFERENCES_NAME = "Settings"

@Module
@InstallIn(SingletonComponent::class)
object DataProviderModule {

    @Provides
    @SettingsPreferencesQualifier
    fun provideSettingsSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return context.getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @DatabaseDispatcherQualifier
    fun provideDatabaseDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    @SettingsDispatcherQualifier
    fun provideSettingsDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
