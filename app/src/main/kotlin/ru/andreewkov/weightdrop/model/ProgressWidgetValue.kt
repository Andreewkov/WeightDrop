package ru.andreewkov.weightdrop.model

import ru.andreewkov.weightdrop.util.roundToDecimals

data class ProgressWidgetValue(
    val start: Float,
    val target: Float,
    val current: Float,
) {

    fun isNegative() = current > start

    fun getDiff() = (start - current).roundToDecimals()

    fun getFactor() = (start - current) / (start - target)
}
