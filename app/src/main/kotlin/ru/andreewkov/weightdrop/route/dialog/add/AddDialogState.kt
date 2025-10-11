package ru.andreewkov.weightdrop.route.dialog.add

import ru.andreewkov.weightdrop.route.base.ScreenState
import java.time.LocalDate

data class AddDialogState(
    val date: LocalDate,
    val weight: Float,
) : ScreenState {

    companion object {
        fun createDefault(): AddDialogState {
            return AddDialogState(
                date = LocalDate.now(),
                weight = 0f,
            )
        }
    }
}
