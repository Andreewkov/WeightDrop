package ru.andreewkov.weightdrop.domain.settings

import ru.andreewkov.weightdrop.data.api.SettingsRepository
import javax.inject.Inject

class UpdateHeightUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    suspend operator fun invoke(height: Int): Result<Unit> {
        return settingsRepository.updateHeight(height)
    }
}
