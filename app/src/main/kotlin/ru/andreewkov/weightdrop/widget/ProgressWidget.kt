package ru.andreewkov.weightdrop.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.model.ProgressWidgetValue
import ru.andreewkov.weightdrop.theme.Dark
import ru.andreewkov.weightdrop.theme.Grey
import ru.andreewkov.weightdrop.theme.Peach
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.roundToDecimals
import kotlin.math.absoluteValue

data class ProgressWidgetColor(
    val inactiveColor: Color,
    val shadowColor: Color,
    val positivePrimaryColor: Color,
    val positiveSecondaryColor: Color,
    val negativePrimaryColor: Color,
    val negativeSecondaryColor: Color,
)

@Composable
fun ProgressWidget(
    color: ProgressWidgetColor,
    value: ProgressWidgetValue,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val lineWidth by remember {
        derivedStateOf { size.width / 8f }
    }
    val primaryColor = remember {
        if (value.isNegative()) color.negativePrimaryColor else color.positivePrimaryColor
    }
    val secondaryColor = remember {
        if (value.isNegative()) color.negativeSecondaryColor else color.positiveSecondaryColor
    }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .drawBehind {
                drawOuterShadow(color.shadowColor)
            }
            .onSizeChanged { size = it },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .clip(CircleShape)
                .shadow(
                    elevation = 0.dp,
                    shape = CircleShape,
                    spotColor = color.shadowColor,
                )
                .background(color.inactiveColor)
                .drawWithCache {
                    val progressBrush = createProgressBrush(
                        color = primaryColor,
                        width = size.width / 16f,
                        ratio = 6f,
                    )
                    onDrawWithContent {
                        drawProgress(lineWidth, value, progressBrush)
                        drawInnerShadow(lineWidth, color.shadowColor)
                        drawContent()
                    }
                },
        ) {
            Text(
                text = value.getDiff().roundToDecimals().absoluteValue.toString(),
                style = TextStyle(
                    color = primaryColor,
                    fontSize = 28.sp,
                    shadow = Shadow(
                        color = secondaryColor,
                        offset = Offset(2f, 2f),
                        blurRadius = 2f,
                    ),
                ),
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

private fun DrawScope.drawProgress(lineWidth: Float, value: ProgressWidgetValue, brush: Brush) {
    drawArc(
        brush = brush,
        startAngle = 270f,
        sweepAngle = value.getFactor() * 360,
        useCenter = false,
        size = Size(size.width - lineWidth, size.height - lineWidth),
        topLeft = Offset(lineWidth / 2, lineWidth / 2),
        style = Stroke(
            width = lineWidth,
            cap = StrokeCap.Butt,
        ),
    )
}

private fun DrawScope.drawOuterShadow(shadowColor: Color) {
    drawCircle(
        brush = Brush.radialGradient(
            1.0f / 1.1f to Color.Transparent,
            1.0f / 1.1f to shadowColor.copy(alpha = 0.2f),
            1.0f to Color.Transparent,
            radius = size.width / 2f * 1.1f,
        ),
        radius = size.width / 2f * 1.1f,
    )
}

private fun DrawScope.drawInnerShadow(lineWidth: Float, shadowColor: Color) {
    drawCircle(
        color = Color.Transparent,
        radius = size.width / 2f - lineWidth,
        blendMode = BlendMode.SrcOut,
    )
    drawCircle(
        brush = Brush.radialGradient(
            0.85f to Color.Transparent,
            1.0f to shadowColor.copy(alpha = 0.2f),
            radius = size.width / 2f - lineWidth,
        ),
        radius = size.width / 2f - lineWidth,
    )
}

private fun createProgressBrush(
    color: Color,
    width: Float,
    ratio: Float,
): Brush {
    val gapWidth = width / ratio
    val brushSize = gapWidth + width
    val stripeStart = gapWidth / brushSize

    return Brush.linearGradient(
        stripeStart to Color.Transparent,
        stripeStart to color,
        start = Offset(0f, 0f),
        end = Offset(brushSize, brushSize / 2),
        tileMode = TileMode.Repeated,
    )
}

@Preview
@Composable
private fun ProgressWidgetPreview() {
    WeightDropTheme {
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Dark),
        )
        ProgressWidget(
            color = ProgressWidgetColor(
                inactiveColor = Peach,
                shadowColor = Grey,
                positivePrimaryColor = Color(0xFF5CDE2A),
                positiveSecondaryColor = Color(0xFF266C0E),
                negativePrimaryColor = Color(0xFFFC643B),
                negativeSecondaryColor = Color(0xFFAD2E0D),
            ),
            value = ProgressWidgetValue(
                target = 80f,
                max = 90.2f,
                current = 84.4f,
            ),
            modifier = Modifier.size(150.dp),
        )
    }
}
