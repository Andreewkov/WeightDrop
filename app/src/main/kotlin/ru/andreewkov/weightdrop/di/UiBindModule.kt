package ru.andreewkov.weightdrop.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.andreewkov.weightdrop.ui.screen.main.MainAppViewModel

@Module
@InstallIn(ViewModelComponent::class)
abstract class UiBindModule {

    //@Binds
    //abstract fun bindInfoActionHandler(viewModel: MainAppViewModel): InfoActionHandler
}