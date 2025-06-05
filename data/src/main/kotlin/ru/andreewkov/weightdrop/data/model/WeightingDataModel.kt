package ru.andreewkov.weightdrop.data.model

import ru.andreewkov.weightdrop.database.WeightingDBO
import java.time.LocalDate

data class WeightingDataModel(
    val value: Float,
    val date: LocalDate,
)

internal fun WeightingDBO.toWeightingDataModel(): WeightingDataModel {
    return WeightingDataModel(
        value = weight,
        date = date,
    )
}

internal fun List<WeightingDBO>.toWeightingDataModels(): List<WeightingDataModel> {
    return map { it.toWeightingDataModel() }
}

internal fun WeightingDataModel.toWeightingDBO(): WeightingDBO {
    return WeightingDBO(
        weight = value,
        date = date,
    )
}
