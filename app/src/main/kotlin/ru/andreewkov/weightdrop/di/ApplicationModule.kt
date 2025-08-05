package ru.andreewkov.weightdrop.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.andreewkov.weightdrop.navigation.Navigation
import ru.andreewkov.weightdrop.navigation.NavigationHolder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationModule {

    @Binds
    @Singleton
    fun bindNavigation(holder: NavigationHolder): Navigation
}
