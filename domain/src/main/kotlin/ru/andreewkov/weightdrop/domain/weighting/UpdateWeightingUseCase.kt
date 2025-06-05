package ru.andreewkov.weightdrop.domain.weighting

import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.data.model.WeightingDataModel
import ru.andreewkov.weightdrop.domain.model.Weighting
import javax.inject.Inject

class UpdateWeightingUseCase @Inject constructor(
    private val weightingRepository: WeightingRepository,
) {

    suspend operator fun invoke(weighting: Weighting): Result<Unit> {
        return weightingRepository.updateWeighting(
            WeightingDataModel(
                value = weighting.value,
                date = weighting.date,
            )
        )
    }
}