package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.ui.util.inRange
import ru.andreewkov.weightdrop.ui.util.pxToDp

data class IndexWithScrollTime(
    val index: Int,
    val scrollMs: Int,
) {

    companion object {
        val default = IndexWithScrollTime(0, 0)
    }
}

@Composable
fun WheelPickerWidget(
    items: List<String>,
    color: Color,
    scrollIndexFlow: StateFlow<IndexWithScrollTime> = MutableStateFlow(IndexWithScrollTime.default),
    displayCount: Int = 5,
    modifier: Modifier = Modifier,
    contentBrush: Brush = createDefaultContentBrush(),
    textAlign: TextAlign = TextAlign.Center,
    onItemSelected: (Int, String) -> Unit = { _, _ -> },
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val scrollState = rememberLazyListState()
    val scrollIndex by scrollIndexFlow.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val itemHeightPx by remember {
        derivedStateOf {
            size.height / displayCount.toFloat()
        }
    }

    val nestedScrollConnection = rememberNestedScrollConnection(
        onPostFling = {
            val index = scrollState.findCurrentIndex(itemHeightPx)
            scrollState.animateScrollToItem(index)
            onItemSelected(index, items[index])
        },
    )
    var isScrolled by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(scrollIndex) {
        val offset = scrollState.firstVisibleItemScrollOffset
        val diffItems = scrollIndex.index - scrollState.firstVisibleItemIndex
        val isScrollInProgress = scrollState.isScrollInProgress

        if ((offset != 0 || diffItems != 0) && !isScrollInProgress) {
            coroutineScope.launch {
                isScrolled = true
                scrollState.stopScroll()
                scrollState.animateScrollBy(
                    value = diffItems * itemHeightPx,
                    animationSpec = tween(
                        durationMillis = scrollIndex.scrollMs.inRange(400, 2000), // (50 * scrollValue.maxDiff).inRange(400, 2000),
                    ),
                )
                scrollState.animateScrollToItem(scrollIndex.index)
                isScrolled = false
            }
        }
    }

    val textStyle by with(LocalDensity.current) {
        remember {
            derivedStateOf {
                TextStyle(
                    color = color,
                    fontSize = itemHeightPx.toSp() * 0.8f,
                    textAlign = textAlign,
                )
            }
        }
    }
    val itemModifier by with(LocalDensity.current) {
        remember {
            derivedStateOf {
                Modifier
                    .fillMaxWidth()
                    .height(itemHeightPx.toDp())
            }
        }
    }

    LazyColumn(
        state = scrollState,
        userScrollEnabled = !isScrolled,
        contentPadding = PaddingValues(vertical = itemHeightPx.pxToDp() * (displayCount - 1) / 2f),
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size = it }
            .graphicsLayer { alpha = 0.99f }
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = contentBrush,
                    blendMode = BlendMode.DstIn,
                )
            }
            .nestedScroll(nestedScrollConnection),
    ) {
        items(if (size == IntSize.Zero) emptyList() else items) { item ->
            Text(
                text = item,
                style = textStyle,
                modifier = itemModifier,
            )
        }
    }
}

private fun createDefaultContentBrush(): Brush {
    return Brush.verticalGradient(
        listOf(
            Color.Transparent,
            Color.Black,
            Color.Transparent,
        ),
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

@Composable
@Preview
private fun WheelPickerWidgetPreviewNumsRect() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp),
        ) {
            WheelPickerWidget(
                items = (0..10).toList().map { it.toString() },
                color = Color.White,
            )
        }
    }
}
