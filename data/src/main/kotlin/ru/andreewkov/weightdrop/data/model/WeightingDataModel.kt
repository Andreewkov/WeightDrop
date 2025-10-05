package ru.andreewkov.weightdrop.data.model

import ru.andreewkov.weightdrop.database.WeightingEntity
import java.time.LocalDate

data class WeightingDataModel(
    val value: Float,
    val date: LocalDate,
)

internal fun WeightingEntity.toDataModel(): WeightingDataModel {
    return WeightingDataModel(
        value = weight,
        date = date,
    )
}

internal fun List<WeightingEntity>.toDataModels(): List<WeightingDataModel> {
    return map { it.toDataModel() }
}

internal fun WeightingDataModel.toEntity(): WeightingEntity {
    return WeightingEntity(
        weight = value,
        date = date,
    )
}
