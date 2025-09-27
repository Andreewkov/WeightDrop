package ru.andreewkov.weightdrop.domain.settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.data.di.SettingsDispatcherQualifier
import javax.inject.Inject

class UpdateTargetWeightUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @SettingsDispatcherQualifier private val settingsDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(weight: Float): Result<Unit> = withContext(settingsDispatcher) {
        settingsRepository.updateTargetWeight(weight)
    }
}
