package ru.andreewkov.weightdrop.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.time.LocalDate

data class SettingItem(
    val type: SettingsItemType,
    @StringRes val titleRes: Int,
    val text: String,
    @DrawableRes val iconRes: Int,
)

enum class SettingsItemType {
    Height,
    StartWeight,
    TargetWeight,
    StartDate,
}

sealed class SettingsUpdateValue<T>(open val value: T) {

    data class HeightValue(
        override val value: Int,
    ) : SettingsUpdateValue<Int>(value)

    data class StartWeightValue(
        override val value: Float,
    ) : SettingsUpdateValue<Float>(value)

    data class TargetWeightValue(
        override val value: Float,
    ) : SettingsUpdateValue<Float>(value)

    data class StartDateValue(
        override val value: LocalDate,
    ) : SettingsUpdateValue<LocalDate>(value)
}
