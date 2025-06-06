package ru.andreewkov.weightdrop.domain.weighting

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.model.toWeightingsFlow
import javax.inject.Inject

class GetWeightingUseCase @Inject constructor(
    private val weightingRepository: WeightingRepository,
) {

    operator fun invoke(): Result<Flow<List<Weighting>>> {
        return weightingRepository.getWeightings().map { it.toWeightingsFlow() }
    }
}