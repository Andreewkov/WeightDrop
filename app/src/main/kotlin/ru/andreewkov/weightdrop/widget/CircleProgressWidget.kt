package ru.andreewkov.weightdrop.widget

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.theme.Peach
import ru.andreewkov.weightdrop.theme.PeachLight
import ru.andreewkov.weightdrop.theme.WeightDropTheme

private const val INSET_BY_WIDTH = 0.08f
private const val STROKE_BY_WIDTH = 0.1f
private const val PROGRESS_BY_WIDTH = 0.12f

@Composable
fun CircleProgressWidget(
    trackColor: Color,
    valueColor: Color,
    @FloatRange(from = -1.0, to = 1.0) progress: Float,
    progressValue: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.aspectRatio(1f),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawStroke(trackColor)
                }
                .blur(4.dp)
                .drawBehind {
                    drawProgress(trackColor, progress)
                },
        )

        Text(
            text = progressValue,
            autoSize = TextAutoSize.StepBased(minFontSize = 12.sp, maxFontSize = 60.sp),
            style = TextStyle(
                fontWeight = FontWeight.W600,
            ),
            color = valueColor,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .align(Alignment.Center),
        )
    }
}

private fun DrawScope.drawStroke(color: Color) {
    inset(size.width * INSET_BY_WIDTH) {
        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(
                width = size.width * STROKE_BY_WIDTH,
            ),
            alpha = 0.25f,
        )
    }
}

private fun DrawScope.drawProgress(
    color: Color,
    @FloatRange(from = -1.0, to = 1.0) progress: Float,
) {
    inset(size.width * INSET_BY_WIDTH) {
        drawArc(
            color = color,
            startAngle = 270f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = Stroke(
                width = size.width * PROGRESS_BY_WIDTH,
                cap = StrokeCap.Round,
            ),
        )
    }
}

@Composable
@Preview
private fun WidgetPreview() {
    WeightDropTheme {
        CircleProgressWidget(
            trackColor = Peach,
            valueColor = PeachLight,
            progress = 0.3f,
            progressValue = "-10.7",
            modifier = Modifier.size(200.dp),
        )
    }
}
