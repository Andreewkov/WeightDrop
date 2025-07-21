package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class IndexWithScrollTime(
    val index: Int,
    val scrollMs: Int = 0,
)

@Composable
fun WheelPickerWidget(
    items: List<String>,
    requiredHeight: Dp,
    textStyle: TextStyle,
    scrollIndexFlow: MutableStateFlow<IndexWithScrollTime>,
    modifier: Modifier = Modifier,
    displayCount: Int = 5,
    contentBrush: Brush = createDefaultContentBrush(),
) {
    val scrollIndex by scrollIndexFlow.collectAsState()
    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollIndex.index,
    )
    val coroutineScope = rememberCoroutineScope()

    val itemHeightPx = with(LocalDensity.current) {
        remember { requiredHeight.toPx() / displayCount }
    }
    val itemsOffsetCount = remember { (displayCount - 1) / 2 }

    val nestedScrollConnection = rememberNestedScrollConnection(
        onPostFling = {
            val index = scrollState.findCurrentIndex(itemHeightPx)
            scrollState.animateScrollToItem(index)
            scrollIndexFlow.emit(IndexWithScrollTime(index))
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
                        durationMillis = scrollIndex.scrollMs,
                    ),
                )
                scrollState.animateScrollToItem(scrollIndex.index)
                isScrolled = false
            }
        }
    }

    val itemModifier = remember {
        Modifier.fillMaxWidth()
            .height(requiredHeight / displayCount)
    }

    LazyColumn(
        state = scrollState,
        userScrollEnabled = !isScrolled,
        overscrollEffect = null,
        modifier = modifier
            .fillMaxWidth()
            .height(requiredHeight)
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
        items(itemsOffsetCount) { Spacer(modifier = itemModifier) }

        items(items) { item ->
            Box(
                modifier = itemModifier,
            ) {
                Text(
                    text = item,
                    style = textStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                )
            }
        }

        items(itemsOffsetCount) { Spacer(modifier = itemModifier) }
    }
}

private fun createDefaultContentBrush(): Brush {
    return Brush.verticalGradient(
        colorStops = arrayOf(
            0.05f to Color.Transparent,
            0.3f to Color.Black.copy(alpha = 0.4f),
            0.46f to Color.Black,
            0.5f to Color.Black,
            0.54f to Color.Black,
            0.7f to Color.Black.copy(alpha = 0.4f),
            0.95f to Color.Transparent,
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
                requiredHeight = 200.dp,
                scrollIndexFlow = createPreviewScrollIndexFlow(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

private fun createPreviewScrollIndexFlow(): MutableStateFlow<IndexWithScrollTime> {
    return MutableStateFlow(IndexWithScrollTime(0, 0))
}
