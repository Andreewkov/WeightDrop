package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.ui.util.inRange
import ru.andreewkov.weightdrop.ui.util.pxToDp
import ru.andreewkov.weightdrop.util.map
import kotlin.math.absoluteValue
import kotlin.math.max

data class WeightPickerNum(
    val integer: Int,
    val fraction: Int,
)

data class WeightPickerNumWithPrev(
    val current: WeightPickerNum,
    val prev: WeightPickerNum,
)

data class WeightPickerWidgetState(
    val num: WeightPickerNumWithPrev,
    val integerRange: IntRange,
    val fractionRange: IntRange,
) {

    private val _currentValue = MutableStateFlow(num)
    val currentValue get() = _currentValue.asStateFlow()

    fun updateValue(num: WeightPickerNum) {
        _currentValue.value = WeightPickerNumWithPrev(
            current = num,
            prev = _currentValue.value.current,
        )
    }
}

@Composable
fun rememberWeightPickerWidgetState(num: WeightPickerNum): WeightPickerWidgetState {
    val integerRange = 0..199
    val fractionRange = 0..9

    return remember {
        WeightPickerWidgetState(
            WeightPickerNumWithPrev(
                current = num,
                prev = WeightPickerNum(0, 0),
            ),
            integerRange = integerRange,
            fractionRange = fractionRange,
        )
    }
}

@Composable
fun WeightPickerWidget(
    state: WeightPickerWidgetState,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val backgroundBrush = remember { createBackgroundBrush() }

    Row(
        modifier = modifier
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithCache {
                val itemHeight = size.height / 7f
                val topY = itemHeight * 2.7f
                val bottomY = itemHeight * 4.3f
                onDrawWithContent {
                    drawContent()
                    drawRect(
                        brush = backgroundBrush,
                        blendMode = BlendMode.DstIn,
                    )

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
        ValueColumn(
            range = state.integerRange,
            currentIndexFlow = state.currentValue.map(scope) {
                PickerScrollValue(
                    current = it.current.integer,
                    maxDiff = max(
                        (it.current.integer - it.prev.integer).absoluteValue,
                        (it.current.fraction - it.prev.fraction).absoluteValue,
                    ),
                )
            },
            color = color,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
            onItemScrolled = { index ->
                state.updateValue(
                    state.currentValue.value.current.copy(integer = index),
                )
            },
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
        ValueColumn(
            range = state.fractionRange,
            currentIndexFlow = state.currentValue.map(scope) {
                PickerScrollValue(
                    current = it.current.fraction,
                    maxDiff = max(
                        (it.current.integer - it.prev.integer).absoluteValue,
                        (it.current.fraction - it.prev.fraction).absoluteValue,
                    ),
                )
            },
            color = color,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
            onItemScrolled = { index ->
                state.updateValue(
                    state.currentValue.value.current.copy(fraction = index),
                )
            },
        )
    }
}

@Composable
private fun ValueColumn(
    range: IntRange,
    currentIndexFlow: StateFlow<PickerScrollValue>,
    color: Color,
    textAlign: TextAlign,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onItemScrolled: (Int) -> Unit = { },
) {
    var columnSize by remember { mutableStateOf(IntSize.Zero) }
    val itemHeightPx by remember {
        derivedStateOf { (columnSize.height / 7f) }
    }
    val scrollValue by currentIndexFlow.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var isScrolled by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(scrollValue) {
        val offset = listState.firstVisibleItemScrollOffset
        val diffItems = scrollValue.current - listState.firstVisibleItemIndex
        val isScrollInProgress = listState.isScrollInProgress

        if ((offset != 0 || diffItems != 0) && !isScrollInProgress) {
            coroutineScope.launch {
                isScrolled = true
                listState.stopScroll()
                listState.animateScrollBy(
                    value = diffItems * itemHeightPx,
                    animationSpec = tween(
                        durationMillis = (50 * scrollValue.maxDiff).inRange(400, 2000),
                    ),
                )
                listState.animateScrollToItem(scrollValue.current)
                isScrolled = false
            }
        }
    }

    val nestedScrollConnection = rememberNestedScrollConnection(
        onPostFling = {
            val index = listState.findCurrentIndex(itemHeightPx)
            listState.animateScrollToItem(index)
            onItemScrolled(index)
        },
    )
    val items = range.map { "$it" }
    val itemModifier by with(LocalDensity.current) {
        remember {
            derivedStateOf {
                Modifier
                    .fillMaxWidth()
                    .height(itemHeightPx.toDp())
            }
        }
    }
    val textStyle by with(LocalDensity.current) {
        remember {
            derivedStateOf {
                TextStyle(
                    color = color,
                    textAlign = textAlign,
                    fontSize = (columnSize.height / 9f).toSp(),
                )
            }
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = itemHeightPx.pxToDp() * 3),
        userScrollEnabled = !isScrolled,
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { columnSize = it }
            .nestedScroll(nestedScrollConnection),
    ) {
        items(items = if (columnSize != IntSize.Zero) items else emptyList()) { item ->
            Text(
                text = item,
                style = textStyle,
                modifier = itemModifier,
            )
        }
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

private fun DrawScope.drawHorizontalLine(color: Color, y: Float) {
    drawLine(
        color = color,
        start = Offset(
            x = 0f,
            y = y,
        ),
        end = Offset(
            x = size.width,
            y = y,
        ),
        strokeWidth = 3f,
    )
}

@Composable
private fun rememberNestedScrollConnection(
    onPostFling: suspend () -> Unit,
): NestedScrollConnection {
    return remember {
        object : NestedScrollConnection {
            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity,
            ): Velocity {
                onPostFling()
                return super.onPostFling(consumed, available)
            }
        }
    }
}

private fun LazyListState.findCurrentIndex(itemHeight: Float): Int {
    return if (firstVisibleItemScrollOffset >= itemHeight / 2) {
        firstVisibleItemIndex + 1
    } else {
        firstVisibleItemIndex
    }
}

private data class PickerScrollValue(
    val current: Int,
    val maxDiff: Int,
)

@Preview
@Composable
fun NumberPickerWidgetPreview() {
    Box(modifier = Modifier.size(100.dp)) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(84, 7),
            ),
            color = Color.White,
        )
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreviewStart() {
    Box(modifier = Modifier.size(100.dp)) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(0, 0),
            ),
            color = Color.White,
        )
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreviewEnd() {
    Box(modifier = Modifier.size(100.dp)) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(199, 9),
            ),
            color = Color.White,
        )
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreviewNarrow() {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(200.dp),
    ) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(104, 1),
            ),
            color = Color.White,
        )
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreviewWide() {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp),
    ) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(56, 6),
            ),
            color = Color.White,
        )
    }
}
