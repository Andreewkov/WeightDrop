package ru.andreewkov.weightdrop.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

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
}

sealed class SettingsUpdateValue {

    data class HeightValue(
        val value: Int,
    ) : SettingsUpdateValue()

    data class StartWeightValue(
        val value: Float,
    ) : SettingsUpdateValue()

    data class TargetWeightValue(
        val value: Float,
    ) : SettingsUpdateValue()
}
