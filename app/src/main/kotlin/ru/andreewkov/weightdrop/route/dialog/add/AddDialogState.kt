package ru.andreewkov.weightdrop.route.dialog.add

import java.time.LocalDate

data class AddDialogState(
    val date: LocalDate,
    val weight: Float,
)
