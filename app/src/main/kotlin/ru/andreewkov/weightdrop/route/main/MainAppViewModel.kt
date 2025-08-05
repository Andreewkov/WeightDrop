package ru.andreewkov.weightdrop.route.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.andreewkov.weightdrop.navigation.Navigation
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    val navigation: Navigation,
) : ViewModel()
