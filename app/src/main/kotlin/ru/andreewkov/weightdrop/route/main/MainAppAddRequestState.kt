package ru.andreewkov.weightdrop.route.main

import java.time.LocalDate

data class MainAppAddRequestState(
    val show: Boolean,
    val date: LocalDate = LocalDate.now(),
)
