package ru.andreewkov.weightdrop.domain.settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.data.di.SettingsDispatcherQualifier
import java.time.LocalDate
import javax.inject.Inject

class UpdateStartDateUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @SettingsDispatcherQualifier private val settingsDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(date: LocalDate): Result<Unit> = withContext(settingsDispatcher) {
        settingsRepository.updateStartDate(date)
    }
}
