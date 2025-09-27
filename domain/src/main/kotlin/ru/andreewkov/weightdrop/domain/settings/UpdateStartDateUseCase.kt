package ru.andreewkov.weightdrop.domain.settings

import ru.andreewkov.weightdrop.data.api.SettingsRepository
import java.time.LocalDate
import javax.inject.Inject

class UpdateStartDateUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    suspend operator fun invoke(date: LocalDate): Result<Unit> {
        return settingsRepository.updateStartDate(date)
    }
}
