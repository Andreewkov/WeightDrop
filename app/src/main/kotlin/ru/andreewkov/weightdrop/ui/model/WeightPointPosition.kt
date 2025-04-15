package ru.andreewkov.weightdrop.ui.model

data class WeightPointPosition(
    val x: Float,
    val y: Float,
    val weightPoint: WeightPoint,
)

data class WeightPointPositionsScope(
    val points: List<WeightPointPosition>,
    val targetY: Float,
)