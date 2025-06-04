package ru.andreewkov.weightdrop.data.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private const val SETTINGS_SHARED_PREFERENCES_NAME = "Settings"

@Module
@InstallIn(SingletonComponent::class)
abstract class DataProviderModule {

    @Provides
    @SettingsQualifier
    fun provideSettingsSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}