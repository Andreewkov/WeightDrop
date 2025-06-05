package ru.andreewkov.weightdrop.domain.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.domain.model.Settings
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke(): Result<Flow<Settings>> {
        return settingsRepository.getSettings().map { it.map { Settings(it.targetWeight, it.height) } }
    }
}