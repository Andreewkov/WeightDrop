package ru.andreewkov.weightdrop.route.dialog.add

import java.time.LocalDate

class AddDialogDatePickerRequestState(
    val show: Boolean,
    val date: LocalDate = LocalDate.now(),
)
