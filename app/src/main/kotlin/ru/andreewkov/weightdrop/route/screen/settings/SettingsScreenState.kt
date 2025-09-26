package ru.andreewkov.weightdrop.route.screen.settings

import ru.andreewkov.weightdrop.model.SettingItem
import ru.andreewkov.weightdrop.route.base.BaseScreenState

sealed class SettingsScreenState : BaseScreenState {

    data object Loading : SettingsScreenState()

    data class Success(
        val items: List<SettingItem>,
    ) : SettingsScreenState()

    data object Failure : SettingsScreenState()
}
