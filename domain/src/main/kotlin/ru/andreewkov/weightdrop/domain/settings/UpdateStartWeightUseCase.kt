package ru.andreewkov.weightdrop.domain.settings

import ru.andreewkov.weightdrop.data.api.SettingsRepository
import javax.inject.Inject

class UpdateStartWeightUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    suspend operator fun invoke(weight: Float): Result<Unit> {
        return settingsRepository.updateStartWeight(weight)
    }
}
