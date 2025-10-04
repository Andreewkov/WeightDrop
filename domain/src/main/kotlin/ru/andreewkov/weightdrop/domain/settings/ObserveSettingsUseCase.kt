package ru.andreewkov.weightdrop.domain.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.toSettings
import javax.inject.Inject

class ObserveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke(): Flow<Settings> {
        return settingsRepository.observeSettings().map { it.toSettings() }
    }
}
