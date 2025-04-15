package ru.andreewkov.weightdrop.ui.model

import androidx.annotation.FloatRange

data class DateDivider(
    val title: String,
    @FloatRange(from = 0.0, to = 1.0) val value: Float,
)

data class DateDividerOld(
    val title: String,
    val value: Float,
)
