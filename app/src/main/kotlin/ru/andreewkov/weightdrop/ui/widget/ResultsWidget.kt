package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.ui.WeightingFormatter
import ru.andreewkov.weightdrop.ui.theme.Dark
import ru.andreewkov.weightdrop.ui.theme.Peach
import ru.andreewkov.weightdrop.ui.theme.PeachLight
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme

data class ResultsWidgetItem(
    val title: String,
    val value: Float,
)

@Composable
fun ResultsWidget(
    results: List<ResultsWidgetItem>,
    background: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val space by with(LocalDensity.current) {
        remember {
            derivedStateOf {
                (size.height / 90f).toDp()
            }
        }
    }
    val textSize by with(LocalDensity.current) {
        remember {
            derivedStateOf {
                (size.height / results.size / 2.5f).toSp()
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(percent = 12))
            .onSizeChanged { size = it }
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            },
    ) {
        results.forEachIndexed { index, item ->
            if (index != 0) {
                Spacer(modifier = Modifier.height(space))
            }
            ResultRow(
                title = item.title,
                value = WeightingFormatter.formatWeightLong(item.value),
                background = background,
                textSize = textSize,
                textColor = textColor,
                spaceWidth = space,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun ResultRow(
    title: String,
    value: String,
    background: Color,
    textSize: TextUnit,
    textColor: Color,
    spaceWidth: Dp,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = textColor,
                fontSize = textSize,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .weight(1f)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(horizontal = 10.dp),
        )
        Spacer(modifier = Modifier.width(spaceWidth))
        Text(
            text = value,
            style = TextStyle(
                color = textColor,
                fontSize = textSize,
            ),
            modifier = Modifier
                .fillMaxHeight()
                .background(background)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(horizontal = 10.dp),
        )
    }
}

@Preview
@Composable
private fun ResultsWidgetPreview1() {
    WeightDropTheme {
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(200.dp)
                .background(Dark),
        ) {
            ResultsWidget(
                results = listOf(
                    ResultsWidgetItem("Начальный вес", 94.4f),
                    ResultsWidgetItem("Максимальный вес", 94.4f),
                    ResultsWidgetItem("Текущий вес", 87f),
                    ResultsWidgetItem("Процент успеха", 15.4f),
                ),
                background = Peach,
                textColor = Dark,
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ResultsWidgetPreview2() {
    WeightDropTheme {
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(200.dp)
                .background(PeachLight),
        ) {
            ResultsWidget(
                results = listOf(
                    ResultsWidgetItem("Начальный вес", 94.4f),
                    ResultsWidgetItem("Максимальный вес", 94.4f),
                    ResultsWidgetItem("Текущий вес", 87f),
                    ResultsWidgetItem("Процент успеха", 15.4f),
                ),
                background = Peach,
                textColor = Dark,
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}
