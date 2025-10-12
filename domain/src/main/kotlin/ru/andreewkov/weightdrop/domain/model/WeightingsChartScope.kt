package ru.andreewkov.weightdrop.domain.model

import java.time.LocalDate

data class WeightingsChartScope(
    val bottomWeight: Float,
    val topWeight: Float,
    val targetWeight: Float?,
    val startWeighting: Weighting,
    val endWeighting: Weighting,
    val dividers: List<Float>,
    val dateDividers: List<LocalDate>,
)
