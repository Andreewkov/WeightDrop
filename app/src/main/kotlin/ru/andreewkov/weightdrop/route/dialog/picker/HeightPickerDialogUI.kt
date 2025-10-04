package ru.andreewkov.weightdrop.route.dialog.picker

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.widget.ButtonContent
import ru.andreewkov.weightdrop.widget.DialogCard
import ru.andreewkov.weightdrop.widget.IndexWithScrollTime
import ru.andreewkov.weightdrop.widget.WheelPickerWidget

@Composable
fun HeightPickerDialogUI(
    initialHeight: Int,
    @StringRes titleRes: Int,
    onHeightPicked: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var currentHeight by remember { mutableIntStateOf(initialHeight) }
    val heightFlow = remember {
        MutableStateFlow(
            IndexWithScrollTime(
                index = (initialHeight - 140).coerceIn(0, 80),
            ),
        )
    }

    LaunchedEffect(Unit) {
        heightFlow.onEach { index ->
            currentHeight = index.index + 140
        }.launchIn(scope)
    }

    DialogCard(
        onDismissRequest = onDismissRequest,
    ) {
        ButtonContent(
            titleRes = titleRes,
            buttonTextRes = R.string.dialog_height_picker_button,
            onButtonClick = {
                onHeightPicked(currentHeight)
                onDismissRequest()
            },
        ) {
            WheelPickerWidget(
                items = (140..220).map { it.toString() },
                requiredHeight = 200.dp,
                textStyle = TextStyle(
                    color = Color.White,
                    textAlign = TextAlign.End,
                    fontSize = 24.sp,
                ), // TODO
                displayCount = 7,
                scrollIndexFlow = heightFlow,
            )
        }
    }
}

@Composable
@WeightDropPreview
private fun ContentPreview() {
    WeightDropTheme {
        ScaffoldPreview {
            HeightPickerDialogUI(
                initialHeight = 187,
                titleRes = R.string.settings_height_title,
                onHeightPicked = { },
                onDismissRequest = { },
            )
        }
    }
}
