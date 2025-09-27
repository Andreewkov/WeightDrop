package ru.andreewkov.weightdrop.domain.settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.data.di.SettingsDispatcherQualifier
import javax.inject.Inject

class UpdateHeightUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @SettingsDispatcherQualifier private val settingsDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(height: Int): Result<Unit> = withContext(settingsDispatcher) {
        settingsRepository.updateHeight(height)
    }
}
