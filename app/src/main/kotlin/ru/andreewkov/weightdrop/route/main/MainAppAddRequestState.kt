package ru.andreewkov.weightdrop.route.main

import java.time.LocalDate

sealed class MainAppAddRequestState {

    data object Hide : MainAppAddRequestState()

    data class Show(val date: LocalDate) : MainAppAddRequestState()
}
