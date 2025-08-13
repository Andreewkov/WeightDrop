package ru.andreewkov.weightdrop.domain.settings

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.toSettingsFlow
import javax.inject.Inject

class ObserveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke(): Result<Flow<Settings>> {
        return settingsRepository.observeSettings().map { it.toSettingsFlow() }
    }
}
