package ru.andreewkov.weightdrop.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.data.impl.SettingsRepositoryImpl
import ru.andreewkov.weightdrop.data.impl.WeightingRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface DataBindModule {

    @Binds
    fun bindWeightingRepository(repository: WeightingRepositoryImpl): WeightingRepository

    @Binds
    fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository
}