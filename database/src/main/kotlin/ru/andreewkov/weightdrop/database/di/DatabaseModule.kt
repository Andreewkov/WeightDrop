package ru.andreewkov.weightdrop.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.andreewkov.weightdrop.database.AppDatabase
import ru.andreewkov.weightdrop.database.LocalDateTypeConverter
import ru.andreewkov.weightdrop.database.WeightingDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideWeightingDao(appDatabase: AppDatabase): WeightingDao {
        return appDatabase.weightingDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        val converter = LocalDateTypeConverter()
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "AppDatabase"
        )
            .addTypeConverter(converter)
            .build()
    }
}