package ru.andreewkov.weightdrop.util

import android.content.res.Configuration
import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun isPortrait() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun TextUnit.spToPx() = with(LocalDensity.current) { this@spToPx.toPx() }

@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun Float.pxToSp() = with(LocalDensity.current) { this@pxToSp.toSp() }

internal fun DrawScope.drawHorizontalLine(
    color: Color,
    y: Float,
    strokeWidth: Float = 1f,
    startMargin: Float = 0f,
    endMargin: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f,
) {
    drawLine(
        color = color,
        start = Offset(startMargin, y),
        end = Offset(size.width - endMargin, y),
        strokeWidth = strokeWidth,
        alpha = alpha,
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

internal fun Modifier.drawHorizontalWheelLines(
    itemsCount: Int,
) = then(
    Modifier.drawWithCache {
        check(itemsCount % 2 == 1)
        val itemExpansion = size.height / itemsCount * 0.9f
        val halfHeight = size.height / 2
        val topY = halfHeight - itemExpansion
        val bottomY = halfHeight + itemExpansion
        onDrawWithContent {
            drawContent()
            drawHorizontalLine(
                color = Color.White,
                y = topY,
                strokeWidth = 2f,
            )
            drawHorizontalLine(
                color = Color.White,
                y = bottomY,
                strokeWidth = 2f,
            )
        }
    },
)

internal fun createWheelBrush(
    itemsCount: Int,
): Brush {
    check(itemsCount % 2 == 1)
    val itemExpansion = 1f / itemsCount * 0.9f
    return Brush.verticalGradient(
        colorStops = arrayOf(
            0.05f to Color.Transparent,
            0.15f to Color(0x4D000000),
            0.4999f - itemExpansion to Color(0x66000000),
            0.5f - itemExpansion to Color.Black,
            0.5f + itemExpansion to Color.Black,
            0.5001f + itemExpansion to Color(0x66000000),
            0.85f to Color(0x4D000000),
            0.95f to Color.Transparent,
        ),
    )
}
