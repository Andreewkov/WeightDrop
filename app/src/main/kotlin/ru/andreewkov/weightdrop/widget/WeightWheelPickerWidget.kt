package ru.andreewkov.weightdrop.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.createWheelBrush
import ru.andreewkov.weightdrop.util.drawHorizontalLine
import ru.andreewkov.weightdrop.util.drawHorizontalWheelLines
import ru.andreewkov.weightdrop.util.getFraction
import ru.andreewkov.weightdrop.util.getInteger
import ru.andreewkov.weightdrop.util.inRange
import kotlin.math.absoluteValue
import kotlin.math.max

@Composable
fun WeightWheelPickerWidget(
    color: Color,
    weight: Float,
    requiredHeight: Dp,
    modifier: Modifier = Modifier,
    onWeightChanged: (Float) -> Unit = { },
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
            .fillMaxSize()
            .drawHorizontalWheelLines(7),
    ) {
        WheelPickerWidget(
            items = integerItems.map { it.toString() },
            requiredHeight = requiredHeight,
            textStyle = TextStyle(
                color = Color.White,
                textAlign = TextAlign.End,
                fontSize = 24.sp,
            ), // TODO
            displayCount = 7,
            scrollIndexFlow = integerFlow,
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
            requiredHeight = requiredHeight,
            textStyle = TextStyle(
                color = Color.White,
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
            ),
            displayCount = 7,
            scrollIndexFlow = fractionFlow,
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
    WeightDropTheme {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp),
        ) {
            WeightWheelPickerWidget(
                color = Color.White,
                weight = 98.8f,
                requiredHeight = 200.dp,
            )
        }
    }
}
