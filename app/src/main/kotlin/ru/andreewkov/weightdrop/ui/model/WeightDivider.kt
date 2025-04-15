package ru.andreewkov.weightdrop.ui.model

import androidx.annotation.FloatRange

data class WeightDivider(
    val title: String,
    @FloatRange(from = 0.0, to = 1.0) val y: Float,
)

data class WeightDividerOld(
    val title: String,
    val y: Float,
)