package ru.andreewkov.weightdrop.ui.util

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.math.roundToInt

fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar {
        it.uppercase()
    }
}

fun Int.inRange(min: Int, max: Int): Int {
    return min(max, max(min, this))
}

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

fun Float.getInteger(): Int {
    return floorToStep(1f).roundToInt()
}

fun Float.getFraction(): Int {
    return (this * 10 % 10).roundToInt()
}
