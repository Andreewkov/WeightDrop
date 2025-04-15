package ru.andreewkov.weightdrop.ui.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

internal fun DrawScope.drawHorizontalLine(
    color: Color,
    y: Float,
    strokeWidth: Float = 1f,
    startMargin: Float = 0f,
    endMargin: Float = 0f,
) {
    drawLine(
        color = color,
        start = Offset(startMargin, y),
        end = Offset(size.width - endMargin, y),
        strokeWidth = strokeWidth,
    )
}

internal fun DrawScope.drawVerticalLine(
    color: Color,
    x: Float,
    strokeWidth: Float = 1f,
    topMargin: Float = 0f,
    bottomMargin: Float = 0f,
) {
    drawLine(
        color = color,
        start = Offset(x, topMargin),
        end = Offset(x, size.height - bottomMargin),
        strokeWidth = strokeWidth,
    )
}