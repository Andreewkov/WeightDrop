package ru.andreewkov.weightdrop.widget

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import ru.andreewkov.weightdrop.WeightingFormatter
import ru.andreewkov.weightdrop.theme.Peach
import ru.andreewkov.weightdrop.theme.WeightDropTheme

private const val INSET_BY_WIDTH = 0.08f
private const val STROKE_BY_WIDTH = 0.1f
private const val PROGRESS_BY_WIDTH = 0.12f

@Composable
fun CircleProgressWidget(
    color: Color,
    @FloatRange(from = -1.0, to = 1.0) progress: Float,
    progressValue: Float,
    modifier: Modifier = Modifier,
) {
    Box {
        Box(
            modifier = modifier
                .fillMaxSize()
                .drawBehind {
                    drawStroke(color)
                }
                .blur(4.dp)
                .drawBehind {
                    drawProgress(color, progress)
                }
        )
        val progress by remember {
            mutableStateOf(
                value = if (progressValue > 0) {
                    "+${WeightingFormatter.formatWeightShort(weight = progressValue)}"
                } else {
                    WeightingFormatter.formatWeightShort(weight = progressValue)
                }
            )
        }
        Text(
            text = progress,
            fontSize = 40.sp,
            style = TextStyle(
                fontWeight = FontWeight.W600,
            ),
            color = color,
            modifier = Modifier.align(Alignment.Center),
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
            alpha = 0.15f,
        )
    }
}

private fun DrawScope.drawProgress(color: Color, @FloatRange(from = -1.0, to = 1.0) progress: Float) {
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
            color = Peach,
            progress = 0.3f,
            progressValue = -10.7f,
            modifier = Modifier.size(200.dp),
        )
    }
}
