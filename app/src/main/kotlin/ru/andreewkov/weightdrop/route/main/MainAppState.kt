package ru.andreewkov.weightdrop.route.main

import androidx.annotation.StringRes
import ru.andreewkov.weightdrop.route.Route

data class MainAppState(
    @StringRes val toolbarTitleRes: Int,
    val selectedBarScreen: Route.BarScreen,
) {

    companion object {
        fun createDefault(): MainAppState {
            return MainAppState(
                toolbarTitleRes = Route.startScreen.titleRes,
                selectedBarScreen = Route.startScreen,
            )
        }
    }
}
