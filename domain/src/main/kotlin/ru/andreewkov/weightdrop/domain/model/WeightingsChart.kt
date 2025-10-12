package ru.andreewkov.weightdrop.domain.model

data class WeightingsChart(
    val scope: WeightingsChartScope,
    val weightings: List<Weighting>,
)
