package ru.andreewkov.weightdrop.domain.weighting

import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.model.toWeighting
import java.time.LocalDate
import javax.inject.Inject

class GetWeightingUseCase @Inject constructor(
    private val weightingRepository: WeightingRepository,
) {

    suspend operator fun invoke(date: LocalDate): Result<Weighting> {
        return weightingRepository.getWeighting(date).map { it.toWeighting() }
    }
}
