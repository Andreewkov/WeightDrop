package ru.andreewkov.weightdrop.ui.screen.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.ui.WeightingFormatter
import ru.andreewkov.weightdrop.ui.WeightingHistoryCalculator
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.util.stubWeightingsMediumThird
import java.time.LocalDate
import java.time.Month

@Composable
fun HistoryScreenUI() {
    val viewModel: HistoryViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsState()

    when (val state = screenState) {
        is HistoryViewModel.ScreenState.History -> {
            HistoryScreenContent(
                weightings = state.weightings,
                onDelete = viewModel::onWeightingDeleted,
            )
        }
        HistoryViewModel.ScreenState.Loading -> Unit
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HistoryScreenContent(
    weightings: List<Weighting>,
    onDelete: (Float, LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val historyItems = remember(weightings) {
        WeightingHistoryCalculator.calculate(weightings)
    }
    var isAnimationRun by remember { mutableStateOf(false) }
    fun onAnimationRun() {
        isAnimationRun = true
    }

    LazyColumn(
        overscrollEffect = null,
        modifier = modifier.fillMaxSize(),
    ) {
        historyItems.forEachIndexed { headerIndex, item ->
            item.header?.let { header ->
                stickyHeader {
                    MonthCard(
                        month = header.month,
                        year = header.year,
                    )
                }
            }

            val wightingSize = item.weightings.size
            itemsIndexed(item.weightings) { index, weighting ->
                val showAnimation = headerIndex == 0 && index == 0

                var isItemVisible by remember { mutableStateOf(true)}
//                AnimatedVisibility(
//                    visible = isItemVisible,
//                    exit = shrinkVertically(
//                        animationSpec = tween(
//                            durationMillis = 300,
//                        )
//                    ),
//                    enter = expandVertically(
//                        animationSpec = tween(
//                            durationMillis = 300
//                        )
//                    )
//                ) {
//
//                }
                WeightingCard(
                    value = weighting.value,
                    date = weighting.date,
                    onDelete = { value, date ->
                        coroutineScope.launch {
                            isItemVisible = false
                            //delay(400)
                            onDelete(value, date)
                        }
                    },
                    showAnimation = showAnimation && !isAnimationRun,
                    onAnimationRun = ::onAnimationRun,
                )
                if (index != wightingSize - 1) {
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
    onDelete: (Float, LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    showAnimation: Boolean = false,
    onAnimationRun: () -> Unit = {},
) {1
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
            SwipeToDismissBoxValue.Settled -> Unit
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
                        .align(Alignment.CenterEnd)
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
                        y = 0
                    )
                }
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(
                    horizontal = dimensionResource(R.dimen.content_screen_padding),
                    vertical = dimensionResource(R.dimen.history_list_items_vertical_padding)
                ),
        ) {
            Text(text = WeightingFormatter.formatDateWithDay(date))
            Text(text = WeightingFormatter.formatWeightLong(value))
        }
    }
}

@Composable
private fun MonthCard(
    month: Month,
    year: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(
                horizontal = dimensionResource(R.dimen.content_screen_padding),
                vertical = dimensionResource(R.dimen.history_list_items_vertical_padding)
            )
    ) {
        Text(text = WeightingFormatter.formatMonthYear(month, year))
    }
}

@Composable
private fun WeightingDivider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .alpha(0.3f)
            .background(MaterialTheme.colorScheme.primary)
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
            modifier = Modifier.width(300.dp)
        ) {
            WeightingCard(
                value = 90f,
                date = LocalDate.of(2025, 5, 30),
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
            modifier = Modifier.width(300.dp)
        ) {
            MonthCard(
                month = Month.APRIL,
                year = 1997,
            )
        }
    }
}

@Composable
@WeightDropPreview
fun HistoryScreenContentPreview() {
    WeightDropTheme {
        Scaffold { innerPading ->
            HistoryScreenContent(
                weightings = stubWeightingsMediumThird,
                modifier = Modifier.padding(innerPading),
                onDelete = { _, _ -> },
            )
        }
    }
}
