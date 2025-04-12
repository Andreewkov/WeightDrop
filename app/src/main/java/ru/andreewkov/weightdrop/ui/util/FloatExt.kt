package ru.andreewkov.weightdrop.ui.util

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

fun Float.floorToStep(step: Float): Float {
    return floor(this / step) * step
}

fun Float.cellToStep(step: Float): Float {
    return ceil(this / step) * step
}

fun Float.hasDecimals(): Boolean {
    return this % 1 != 0f
}

fun Float.roundToDecimals(): Float {
    return round(this * 10) / 10
}