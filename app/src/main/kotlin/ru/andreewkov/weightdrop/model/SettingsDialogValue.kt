package ru.andreewkov.weightdrop.model

import java.time.LocalDate

sealed class SettingsDialogValue<Value>(open val value: Value) {

    data class HeightValue(
        override val value: Int,
    ) : SettingsDialogValue<Int>(value)

    data class StartWeightValue(
        override val value: Float,
    ) : SettingsDialogValue<Float>(value)

    data class TargetWeightValue(
        override val value: Float,
    ) : SettingsDialogValue<Float>(value)

    data class StartDateValue(
        override val value: LocalDate,
    ) : SettingsDialogValue<LocalDate>(value)
}
