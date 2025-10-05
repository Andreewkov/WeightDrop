package ru.andreewkov.weightdrop.route.screen.history

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.WeightingFormatter
import ru.andreewkov.weightdrop.domain.model.HistoryBlock
import ru.andreewkov.weightdrop.domain.weighting.CalculateHistoryBlocksUseCase
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.util.stubWeightingsMediumThird
import java.time.LocalDate
import java.time.Month

@Composable
fun HistoryScreenUI(
    onCardClick: (LocalDate) -> Unit,
) {
    val viewModel: HistoryScreenViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsState()

    when (val state = screenState) {
        is HistoryScreenState.Success -> {
            Content(
                blocks = state.blocks,
                onCardClick = { _, date ->
                    onCardClick(date)
                },
                onDelete = viewModel::onWeightingDeleted,
            )
        }
        HistoryScreenState.Failure -> Unit // TODO
        HistoryScreenState.Loading -> Unit
        HistoryScreenState.Empty -> Unit
    }
}

@Composable
private fun Content(
    blocks: List<HistoryBlock>,
    onCardClick: (Float, LocalDate) -> Unit,
    onDelete: (Float, LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var isAnimationRun by remember { mutableStateOf(false) }

    fun onAnimationRun() {
        isAnimationRun = true
    }

    LazyColumn(
        overscrollEffect = null,
        modifier = modifier.fillMaxSize(),
    ) {
        blocks.forEachIndexed { headerIndex, headerItem ->
            headerItem.header?.let { header ->
                stickyHeader {
                    MonthCard(
                        month = header.month,
                        year = header.year,
                        diff = header.diff,
                    )
                }
            }

            val itemSize = headerItem.weightingItems.size
            itemsIndexed(headerItem.weightingItems) { index, item ->
                val showAnimation = headerIndex == 0 && index == 0

                var isItemVisible by remember { mutableStateOf(true) }
                WeightingCard(
                    value = item.weighting.value,
                    date = item.weighting.date,
                    diff = item.diff,
                    onDelete = { value, date ->
                        coroutineScope.launch {
                            isItemVisible = false
                            onDelete(value, date)
                        }
                    },
                    showAnimation = showAnimation && !isAnimationRun,
                    onAnimationRun = ::onAnimationRun,
                    modifier = Modifier.clickable {
                        onCardClick(
                            item.weighting.value,
                            item.weighting.date,
                        )
                    },
                )
                if (index != itemSize - 1) {
                    WeightingDivider()
                }
            }
        }
    }
}

@Composable
private fun WeightingCard(
    value: Float,
    date: LocalDate,
    diff: Float,
    onDelete: (Float, LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    showAnimation: Boolean = false,
    onAnimationRun: () -> Unit = {},
) {
    val dismissState = rememberSwipeToDismissBoxState()
    LaunchedEffect(date) {
        dismissState.snapTo(SwipeToDismissBoxValue.Settled)
    }
    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> {
                onDelete(value, date)
            }
            SwipeToDismissBoxValue.StartToEnd,
            SwipeToDismissBoxValue.Settled,
            -> Unit
        }
    }

    val offsetAnimatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (showAnimation) {
            onAnimationRun()
            offsetAnimatable.runOffsetAnimation()
        }
    }
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                )
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = (50.dp.toPx() * offsetAnimatable.value).toInt(),
                        y = 0,
                    )
                }
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(
                    horizontal = dimensionResource(R.dimen.content_screen_padding),
                    vertical = dimensionResource(R.dimen.history_list_items_vertical_padding),
                ),
        ) {
            Text(text = WeightingFormatter.formatDateShortWithDay(date))
            Text(text = WeightingFormatter.formatWeightLong(diff))
            Text(text = WeightingFormatter.formatWeightLong(value))
        }
    }
}

@Composable
private fun MonthCard(
    month: Month,
    year: Int,
    diff: Float,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(
                horizontal = dimensionResource(R.dimen.content_screen_padding),
                vertical = dimensionResource(R.dimen.history_list_items_vertical_padding),
            ),
    ) {
        Text(text = WeightingFormatter.formatMonthYear(month, year))
        Text(text = WeightingFormatter.formatWeightLong(diff))
    }
}

@Composable
private fun WeightingDivider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .alpha(0.3f)
            .background(MaterialTheme.colorScheme.primary),
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.runOffsetAnimation() {
    animateTo(
        targetValue = -1f,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(
                durationMillis = 300,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    animateTo(
        targetValue = 0f,
        animationSpec = repeatable(
            iterations = 1,
            animation = tween(
                durationMillis = 1000,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
    )
}

@Composable
@Preview
fun WeightCardPreview() {
    WeightDropTheme {
        Surface(
            modifier = Modifier.width(300.dp),
        ) {
            WeightingCard(
                value = 90f,
                date = LocalDate.of(2025, 5, 30),
                diff = 0f,
                onDelete = { _, _ -> },
            )
        }
    }
}

@Composable
@Preview
fun MonthCardPreview() {
    WeightDropTheme {
        Surface(
            modifier = Modifier.width(300.dp),
        ) {
            MonthCard(
                month = Month.APRIL,
                year = 1997,
                diff = 0f,
            )
        }
    }
}

@Composable
@WeightDropPreview
fun ContentPreview() {
    WeightDropTheme {
        ScaffoldPreview {
            val blocks = remember {
                runBlocking {
                    CalculateHistoryBlocksUseCase(Dispatchers.Default).invoke(stubWeightingsMediumThird)
                }.getOrThrow()
            }
            Content(
                blocks = blocks,
                onCardClick = { _, _ -> },
                onDelete = { _, _ -> },
            )
        }
    }
}
