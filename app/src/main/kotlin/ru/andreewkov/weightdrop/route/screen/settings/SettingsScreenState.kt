package ru.andreewkov.weightdrop.route.screen.settings

import ru.andreewkov.weightdrop.model.SettingItem

sealed class SettingsScreenState {

    data object Loading : SettingsScreenState()

    data class Success(
        val items: List<SettingItem>,
    ) : SettingsScreenState()

    data object Failure : SettingsScreenState()
}
