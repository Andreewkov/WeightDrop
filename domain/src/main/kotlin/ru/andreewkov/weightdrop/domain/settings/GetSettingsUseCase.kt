package ru.andreewkov.weightdrop.domain.settings

import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.toSettings
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke(): Settings {
        return settingsRepository.getSettings().toSettings()
    }
}
