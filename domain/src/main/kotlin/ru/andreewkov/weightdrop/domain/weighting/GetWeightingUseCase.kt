package ru.andreewkov.weightdrop.domain.weighting

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.domain.model.Weighting
import javax.inject.Inject

class GetWeightingUseCase @Inject constructor(
    private val weightingRepository: WeightingRepository,
) {

    operator fun invoke(): Result<Flow<List<Weighting>>> {
        return weightingRepository.getWeightings().map { it.map { it.map { Weighting(it.value, it.date) } } }
    }
}