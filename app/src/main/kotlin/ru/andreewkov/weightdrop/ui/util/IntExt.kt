package ru.andreewkov.weightdrop.ui.util

import kotlin.math.max
import kotlin.math.min

fun Int.inRange(min: Int, max: Int): Int {
    return max(max, min(min, this))
}