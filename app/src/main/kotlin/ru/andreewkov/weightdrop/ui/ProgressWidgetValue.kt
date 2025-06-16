package ru.andreewkov.weightdrop.ui

import ru.andreewkov.weightdrop.ui.util.roundToDecimals

data class ProgressWidgetValue(
    val target: Float,
    val max: Float,
    val current: Float,
) {

    fun isNegative() = current > max

    fun getDiff() = (current - max).roundToDecimals()

    fun getFactor() = (max - current) / (max - target)
}
