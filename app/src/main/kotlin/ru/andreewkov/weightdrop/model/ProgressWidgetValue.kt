package ru.andreewkov.weightdrop.model

import ru.andreewkov.weightdrop.util.roundToDecimals

data class ProgressWidgetValue(
    val target: Float,
    val max: Float,
    val current: Float,
) {

    fun isNegative() = current > max

    fun getDiff() = (current - max).roundToDecimals()

    fun getFactor() = (max - current) / (max - target)
}
