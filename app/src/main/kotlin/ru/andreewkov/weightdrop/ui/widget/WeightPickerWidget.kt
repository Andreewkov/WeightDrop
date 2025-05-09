package ru.andreewkov.weightdrop.ui.widget
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.ui.util.pxToDp
import ru.andreewkov.weightdrop.ui.util.pxToSp
import java.util.LinkedList

data class WeightPickerNum(
    val integer: Int,
    val fraction: Int,
)

class WeightPickerWidgetState(
    num: WeightPickerNum,
    val integerRange: IntRange,
    val fractionRange: IntRange,
) {

    private val _currentValue = MutableStateFlow(num)
    val currentValue get() = _currentValue.asStateFlow()

    fun updateValue(num: WeightPickerNum) {
        _currentValue.value = num
    }
}

@Composable
fun rememberWeightPickerWidgetState(num: WeightPickerNum): WeightPickerWidgetState {
    val integerRange = 0..199
    val fractionRange = 0..9

    return remember { WeightPickerWidgetState(num, integerRange, fractionRange) }
}

@Composable
fun WeightPickerWidget(
    state: WeightPickerWidgetState,
    primaryColor: Color,
    secondaryColor: Color,
    modifier: Modifier = Modifier,
) {
    val backgroundBrush = remember { createBackgroundBrush() }
    val currentValue by state.currentValue.collectAsState()

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
                        color = primaryColor,
                        y = topY,
                    )
                    drawHorizontalLine(
                        color = primaryColor,
                        y = bottomY,
                    )
                }
            }
    ) {
        ValueColumn(
            color = primaryColor,
            range = state.integerRange,
            prefix = "",
            currentIndex = currentValue.integer,
            modifier = Modifier.weight(1f),
            getItemModifier = { Modifier.align(Alignment.CenterEnd) },
            onItemScrolled = { index ->
                state.updateValue(
                    state.currentValue.value.copy(integer = index)
                )
            }
        )
        Spacer(Modifier.size(14.dp))
        ValueColumn(
            color = primaryColor,
            range = state.fractionRange,
            prefix = ".",
            currentIndex = currentValue.fraction,
            modifier = Modifier.weight(1f),
            getItemModifier = { Modifier.align(Alignment.CenterStart) },
            onItemScrolled = { index ->
                state.updateValue(
                    state.currentValue.value.copy(fraction = index)
                )
            }
        )
    }
}

@Composable
private fun ValueColumn(
    color: Color,
    range: IntRange,
    prefix: String,
    currentIndex: Int,
    modifier: Modifier = Modifier,
    getItemModifier: BoxScope.() -> Modifier = { Modifier },
    onItemScrolled: (Int) -> Unit = { },
) {
    var columnSize by remember { mutableStateOf(IntSize.Zero) }
    val itemHeight by remember {
        derivedStateOf { (columnSize.height / 7f) }
    }
    val fontSize = (columnSize.height / 9f).pxToSp()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isScrolled by remember {
        mutableStateOf(false)
    }
    val isScrollInProgress by remember {
        derivedStateOf { listState.isScrollInProgress || isScrolled }
    }

    LaunchedEffect(currentIndex) {
        if (currentIndex > 0 && listState.firstVisibleItemIndex != currentIndex && !isScrollInProgress) {
            coroutineScope.launch {
                isScrolled = true
                listState.animateScrollBy(
                    value = (currentIndex - listState.firstVisibleItemIndex) * itemHeight,
                    animationSpec = tween(durationMillis = 500)
                )
                delay(500)
                isScrolled = false
            }
        }
    }
    LaunchedEffect(isScrollInProgress) {
        if (isScrollInProgress) return@LaunchedEffect
        if (listState.firstVisibleItemScrollOffset != 0) {
            val currentIndex = if (listState.firstVisibleItemScrollOffset >= itemHeight / 2) {
                listState.firstVisibleItemIndex + 1
            } else {
                listState.firstVisibleItemIndex
            }
            listState.animateScrollToItem(currentIndex)
        }
    }
    val nestedScrollConnection = rememberNestedScrollConnection(
        onPostFling = {
            onItemScrolled(listState.firstVisibleItemIndex)
        }
    )
    val contentPadding by with(LocalDensity.current) {
        remember {
            derivedStateOf {
                PaddingValues(vertical = (itemHeight * 3).toDp())
            }
        }
    }
    val items = range.map { "$prefix$it" }

    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { columnSize = it }
            .nestedScroll(nestedScrollConnection)
    ) {
        items(items = if (columnSize != IntSize.Zero) items else emptyList()) { item ->
            Log.d("rrrttt", "item $item")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight.pxToDp())
            ) {
                Text(
                    text = item,
                    style = TextStyle(
                        color = color,
                        fontSize = fontSize,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = getItemModifier()
                )
            }
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
    val stack = LinkedList<Char>()
}

@Composable
private fun rememberNestedScrollConnection(
    onPostFling: suspend () -> Unit,
): NestedScrollConnection {
    return remember {
        object : NestedScrollConnection {
            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity {
                onPostFling()
                return super.onPostFling(consumed, available)
            }
        }
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreview() {
    Box(modifier = Modifier.size(100.dp)) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(84, 7)
            ),
            primaryColor = Color.White,
            secondaryColor = Color.LightGray,
        )
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreviewStart() {
    Box(modifier = Modifier.size(100.dp)) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(0, 0)
            ),
            primaryColor = Color.White,
            secondaryColor = Color.LightGray,
        )
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreviewEnd() {
    Box(modifier = Modifier.size(100.dp)) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(199, 9)
            ),
            primaryColor = Color.White,
            secondaryColor = Color.LightGray,
        )
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreviewNarrow() {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(200.dp)
    ) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(104, 1)
            ),
            primaryColor = Color.White,
            secondaryColor = Color.LightGray,
        )
    }
}

@Preview
@Composable
fun NumberPickerWidgetPreviewWide() {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
    ) {
        WeightPickerWidget(
            state = rememberWeightPickerWidgetState(
                num = WeightPickerNum(56, 6)
            ),
            primaryColor = Color.White,
            secondaryColor = Color.LightGray,
        )
    }
}
