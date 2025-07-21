package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import java.time.LocalDate
import java.time.Month
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlin.math.min

typealias Style = java.time.format.TextStyle

@Composable
fun DateWheelPickerWidget(
    date: LocalDate,
    color: Color,
    requiredHeight: Dp,
    onDateChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var requiredDayCount by remember { mutableIntStateOf(0) }
    var dayCount by remember { mutableIntStateOf(30) }

    val months = remember {
        Month.entries.map { it.getDisplayName(Style.FULL, Locale("ru", "RU")) }
    }
    val years = remember {
        (1970..2030).toList().map { it.toString() }
    }
    val days by remember {
        derivedStateOf { (1..dayCount).toList().map { it.toString() } }
    }

    val dayFlow = remember { MutableStateFlow(IndexWithScrollTime(date.dayIndex)) }
    val monthFlow = remember { MutableStateFlow(IndexWithScrollTime(date.monthIndex)) }
    val yearFlow = remember { MutableStateFlow(IndexWithScrollTime(date.yearIndex)) }

    LaunchedEffect(Unit) {
        delay(500)

        fun onDateChanged() {
            onDateChanged(
                LocalDate.of(
                    yearFlow.value.indexOfYear,
                    monthFlow.value.indexOfMonth,
                    if (requiredDayCount != 0) {
                        min(dayFlow.value.indexOfDay, requiredDayCount)
                    } else {
                        dayFlow.value.indexOfDay
                    },
                ),
            )
        }
        dayFlow.onEach { onDateChanged() }.launchIn(this)

        monthFlow.onEach { month ->
            requiredDayCount = LocalDate.of(
                yearFlow.value.indexOfYear,
                monthFlow.value.indexOfMonth,
                1,
            ).with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth
            onDateChanged()
        }.launchIn(this)

        yearFlow.onEach { onDateChanged() }.launchIn(this)
    }
    LaunchedEffect(requiredDayCount) {
        if (requiredDayCount == 0) return@LaunchedEffect

        var delay = 0L
        if (dayFlow.value.indexOfDay > requiredDayCount) {
            dayFlow.emit(IndexWithScrollTime(requiredDayCount - 1, 200))
            delay = 500
        }
        delay(delay)
        dayCount = requiredDayCount
        requiredDayCount = 0
    }

    val textStyle = remember {
        TextStyle(
            color = color,
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        WheelPickerWidget(
            items = days,
            textStyle = textStyle,
            scrollIndexFlow = dayFlow,
            requiredHeight = requiredHeight,
            modifier = Modifier.weight(1f),
        )
        WheelPickerWidget(
            items = months,
            requiredHeight = requiredHeight,
            textStyle = textStyle,
            scrollIndexFlow = monthFlow,
            modifier = Modifier.weight(2f),
        )
        WheelPickerWidget(
            items = years,
            requiredHeight = requiredHeight,
            textStyle = textStyle,
            scrollIndexFlow = yearFlow,
            modifier = Modifier.weight(1f),
        )
    }
}

private val LocalDate.dayIndex get() = dayOfMonth - 1

private val LocalDate.monthIndex get() = monthValue - 1

private val LocalDate.yearIndex get() = year - 1971

private val IndexWithScrollTime.indexOfDay get() = index + 1

private val IndexWithScrollTime.indexOfMonth get() = index + 1

private val IndexWithScrollTime.indexOfYear get() = index + 1971

@Composable
@Preview
private fun DateWheelPickerWidgetPreviw() {
    WeightDropTheme {
        Box(
            modifier = Modifier
                .width(400.dp)
                .height(200.dp),
        ) {
            DateWheelPickerWidget(
                color = Color.White,
                date = LocalDate.now(),
                requiredHeight = 200.dp,
                onDateChanged = { },
            )
        }
    }
}
