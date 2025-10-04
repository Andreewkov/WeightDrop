package ru.andreewkov.weightdrop.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SettingItem(
    val type: SettingsItemType,
    @StringRes val titleRes: Int,
    val text: String?,
    @DrawableRes val iconRes: Int,
    @StringRes val textPostfixRes: Int? = null,
)

enum class SettingsItemType {
    Height,
    StartWeight,
    TargetWeight,
    StartDate,
}
