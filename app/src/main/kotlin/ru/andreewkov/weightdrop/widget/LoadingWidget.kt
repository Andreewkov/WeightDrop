package ru.andreewkov.weightdrop.widget

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.theme.Peach
import ru.andreewkov.weightdrop.theme.WeightDropTheme

@Composable
fun LoadingWidget(
    color: Color,
    modifier: Modifier = Modifier,
) {
    val sizeAnimatable = remember { Animatable(1f) }
    val rotationAnimatable = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        sizeAnimatable.animateTo(
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    1600,
                    easing = LinearEasing,
                ), // tween animation with a duration of 1000ms
                repeatMode = RepeatMode.Restart, // repeat the animation in reverse
            ),
        )
    }

    LaunchedEffect(Unit) {
        rotationAnimatable.animateTo(
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    3000,
                    easing = LinearEasing,
                ), // tween animation with a duration of 1000ms
                repeatMode = RepeatMode.Restart, // repeat the animation in reverse
            ),
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawContent(
                    sizeProgress = sizeAnimatable.value,
                    rotationProgress = rotationAnimatable.value,
                    color = color,
                )
            },
    )
}

private fun DrawScope.drawContent(
    @FloatRange(from = 0.0, to = 1.0) sizeProgress: Float,
    @FloatRange(from = 0.0, to = 1.0) rotationProgress: Float,
    color: Color,
) {
    val count = 15
    val degrees = 360f / count
    repeat(count) { i ->
        rotate(degrees * i + degrees * rotationProgress) {
            drawCircle(
                color = color,
                radius = size.height / 15 * ((sizeProgress + i.toFloat() / count) % 1f),
                center = Offset(size.width / 4, size.height / 4),
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview0() {
    WeightDropTheme {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawContent(
                sizeProgress = 0f,
                rotationProgress = 0f,
                color = Peach,
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview02() {
    WeightDropTheme {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawContent(
                sizeProgress = 0.2f,
                rotationProgress = 0f,
                color = Peach,
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview04() {
    WeightDropTheme {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawContent(
                sizeProgress = 0.4f,
                rotationProgress = 0f,
                color = Peach,
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview06() {
    WeightDropTheme {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawContent(
                sizeProgress = 0.6f,
                rotationProgress = 0f,
                color = Peach,
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview08() {
    WeightDropTheme {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawContent(
                sizeProgress = 0.8f,
                rotationProgress = 0f,
                color = Peach,
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview1() {
    WeightDropTheme {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawContent(
                sizeProgress = 1f,
                rotationProgress = 0f,
                color = Peach,
            )
        }
    }
}
