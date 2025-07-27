package ru.andreewkov.weightdrop.model

import java.time.LocalDate

data class WeightPoint(
    val date: LocalDate,
    val weightValue: Float?,
    val drawDivider: Boolean,
)
