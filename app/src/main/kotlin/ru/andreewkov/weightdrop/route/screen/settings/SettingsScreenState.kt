package ru.andreewkov.weightdrop.route.screen.settings

import ru.andreewkov.weightdrop.model.SettingItem
import ru.andreewkov.weightdrop.model.SettingsDialogValue
import ru.andreewkov.weightdrop.route.base.ScreenState

data class SettingsScreenState(
    val content: SettingsContentState,
    val displayedDialog: SettingsDialogValue<*>? = null,
) : ScreenState

sealed class SettingsContentState : ScreenState {

    data object Loading : SettingsContentState()

    data class Success(
        val items: List<SettingItem>,
    ) : SettingsContentState()

    data object Failure : SettingsContentState()
}
