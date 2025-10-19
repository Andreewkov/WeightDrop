package ru.andreewkov.weightdrop.domain.weighting

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.model.toWeightings
import javax.inject.Inject

class ObserveWeightingsUseCase @Inject constructor(
    private val weightingRepository: WeightingRepository,
) {

    operator fun invoke(): Flow<List<Weighting>> {
        return weightingRepository.observeWeightings().map { it.toWeightings() }
    }
}
