package ru.andreewkov.weightdrop.route.screen.settings

import ru.andreewkov.weightdrop.model.SettingItem
import ru.andreewkov.weightdrop.model.SettingsDialogValue
import ru.andreewkov.weightdrop.route.base.BaseScreenState

data class SettingsScreenState(
    val content: SettingsContentState,
    val displayedDialog: SettingsDialogValue<*>? = null,
) : BaseScreenState

sealed class SettingsContentState : BaseScreenState {

    data object Loading : SettingsContentState()

    data class Success(
        val items: List<SettingItem>,
    ) : SettingsContentState()

    data object Failure : SettingsContentState()
}
