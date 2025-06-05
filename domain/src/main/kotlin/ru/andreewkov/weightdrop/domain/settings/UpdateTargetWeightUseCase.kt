package ru.andreewkov.weightdrop.domain.settings

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import javax.inject.Inject

class UpdateTargetWeightUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    suspend operator fun invoke(weight: Float): Result<Unit> {
        return settingsRepository.updateTargetWeight(weight)
    }
}