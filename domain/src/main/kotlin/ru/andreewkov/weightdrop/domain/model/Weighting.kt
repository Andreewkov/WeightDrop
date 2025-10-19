package ru.andreewkov.weightdrop.domain.model

import ru.andreewkov.weightdrop.data.model.WeightingDataModel
import java.time.LocalDate

data class Weighting(
    val value: Float,
    val date: LocalDate,
)

internal fun WeightingDataModel.toWeighting(): Weighting {
    return Weighting(value, date)
}

internal fun List<WeightingDataModel>.toWeightings(): List<Weighting> {
    return map { it.toWeighting() }
}
