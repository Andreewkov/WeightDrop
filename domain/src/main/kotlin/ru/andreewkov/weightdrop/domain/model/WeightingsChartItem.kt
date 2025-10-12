package ru.andreewkov.weightdrop.domain.model

import java.time.LocalDate

data class WeightingsChartItem(
    val date: LocalDate,
    val weightValue: Float?,
    val drawDivider: Boolean,
)
