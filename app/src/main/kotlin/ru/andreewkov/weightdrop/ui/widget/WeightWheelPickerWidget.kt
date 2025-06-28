package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import ru.andreewkov.weightdrop.ui.util.drawHorizontalLine
import ru.andreewkov.weightdrop.ui.util.getFraction
import ru.andreewkov.weightdrop.ui.util.getInteger
import ru.andreewkov.weightdrop.ui.util.inRange
import kotlin.math.absoluteValue
import kotlin.math.max

@Composable
fun WeightWheelPickerWidget(
    color: Color,
    weight: Float,
    onWeightChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val integerItems = (0..199).toList()
    val fractionItems = (0..9).toList()
    val integerFlow = remember { MutableStateFlow(IndexWithScrollTime(weight.getInteger())) }
    val fractionFlow = remember { MutableStateFlow(IndexWithScrollTime(weight.getFraction())) }
    LaunchedEffect(Unit) {
        integerFlow.collect {
            onWeightChanged(
                integerItems[it.index] + fractionItems[fractionFlow.value.index] / 10f,
            )
        }
    }
    LaunchedEffect(Unit) {
        fractionFlow.collect {
            onWeightChanged(
                integerItems[integerFlow.value.index] + fractionItems[it.index] / 10f,
            )
        }
    }
    LaunchedEffect(weight) {
        val integer = weight.getInteger()
        val fraction = weight.getFraction()
        val maxDiff = max(
            (integerFlow.value.index - integer).absoluteValue,
            (fractionFlow.value.index - fraction).absoluteValue,
        )
        val scrollTime = (50 * maxDiff).inRange(400, 2000)
        integerFlow.emit(IndexWithScrollTime(integer, scrollTime))
        fractionFlow.emit(IndexWithScrollTime(fraction, scrollTime))
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawWithCache {
                val itemHeight = size.height / 7f
                val topY = itemHeight * 2.7f
                val bottomY = itemHeight * 4.3f
                onDrawWithContent {
                    drawContent()
                    drawHorizontalLine(
                        color = color,
                        y = topY,
                    )
                    drawHorizontalLine(
                        color = color,
                        y = bottomY,
                    )
                }
            },
    ) {
        val contentBrush = createBackgroundBrush()

        WheelPickerWidget(
            items = integerItems.map { it.toString() },
            color = Color.White,
            displayCount = 7,
            scrollIndexFlow = integerFlow,
            contentBrush = contentBrush,
            textAlign = TextAlign.End,
            textFactor = 0.85f,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = ".",
            style = TextStyle(
                color = color,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .width(20.dp)
                .align(Alignment.CenterVertically),
        )

        WheelPickerWidget(
            items = fractionItems.map { it.toString() },
            color = Color.White,
            displayCount = 7,
            scrollIndexFlow = fractionFlow,
            contentBrush = contentBrush,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun createBackgroundBrush(): Brush {
    return Brush.verticalGradient(
        colorStops = arrayOf(
            0.05f to Color.Transparent,
            0.15f to Color(0x4D000000),
            1f / 7 * 2.7f to Color(0x66000000),
            1f / 7 * 2.71f to Color.Black,
            1f / 7 * 4.311f to Color.Black,
            1f / 7 * 4.3f to Color(0x66000000),
            0.85f to Color(0x4D000000),
            0.95f to Color.Transparent,
        ),
    )
}

@Composable
@Preview
private fun WheelPickerWidgetPreviewNumsRect() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp),
        ) {
            WeightWheelPickerWidget(
                color = Color.White,
                weight = 98.8f,
                onWeightChanged = { },
            )
        }
    }
}
