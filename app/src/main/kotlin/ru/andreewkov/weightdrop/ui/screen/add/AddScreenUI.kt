package ru.andreewkov.weightdrop.ui.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.WeightingFormatter
import ru.andreewkov.weightdrop.ui.screen.dialog.DatePickerDialogUI
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.util.getDecimals
import ru.andreewkov.weightdrop.ui.util.roundToDecimals
import ru.andreewkov.weightdrop.ui.widget.ValuePanelWidget
import ru.andreewkov.weightdrop.ui.widget.WeightPickerNum
import ru.andreewkov.weightdrop.ui.widget.WeightPickerWidget
import ru.andreewkov.weightdrop.ui.widget.WeightPickerWidgetState
import ru.andreewkov.weightdrop.ui.widget.rememberWeightPickerWidgetState
import java.time.LocalDate

@Composable
fun AddScreenUI(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: AddViewModel = hiltViewModel()
    val state by viewModel.screenState.collectAsState()
    val showDateDialog by viewModel.showDateDialog.collectAsState()

    val weightPickerWidgetState = rememberWeightPickerWidgetState(
        num = WeightPickerNum(
            integer = state.weight.roundToDecimals().toInt(),
            fraction = state.weight.getDecimals().toInt(),
        ),
    )
    LaunchedEffect(state) {
        weightPickerWidgetState.updateValue(
            WeightPickerNum(
                integer = state.weight.roundToDecimals().toInt(),
                fraction = state.weight.getDecimals().toInt(),
            ),
        )
    }

    Card {
        AddDialogContent(
            date = state.date,
            onDateClick = viewModel::onDatePickerDialogRequest,
            weightPickerWidgetState = weightPickerWidgetState,
            onWeightChanged = viewModel::onWeightChanged,
            onAddClick = {
                viewModel.onWeightAddClick()
                navController.popBackStack()
            },
            modifier = modifier,
        )
    }

    if (showDateDialog) {
        DatePickerDialogUI(
            onDismissRequest = viewModel::onDatePickerDialogDismissRequest,
            onConfirmClick = viewModel::onDatePickerDialogConfirm,
        )
    }
}

@Composable
private fun AddDialogContent(
    date: LocalDate,
    onDateClick: () -> Unit,
    weightPickerWidgetState: WeightPickerWidgetState,
    onWeightChanged: (Float) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weight by weightPickerWidgetState.currentValue.collectAsState()
    LaunchedEffect(weight) {
        onWeightChanged(weight.current.integer + weight.current.fraction.toFloat() / 10)
    }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = 32.dp,
                vertical = 16.dp,
            ),
    ) {
        WeightPickerWidget(
            state = weightPickerWidgetState,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .heightIn(max = 200.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.size(12.dp))

        ValuePanelWidget(
            title = stringResource(R.string.screen_add_date_title),
            text = WeightingFormatter.formatDateLong(date),
            tintColor = MaterialTheme.colorScheme.primary,
            iconPainter = painterResource(R.drawable.ic_calendar),
            onClick = {
                onDateClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
        )

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
            modifier = Modifier
                .height(42.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.add_dialog_add_button),
                color = MaterialTheme.colorScheme.surface,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                ),
            )
        }
    }
}

@Composable
@WeightDropPreview
private fun AddScreenContentPreview() {
    WeightDropTheme {
        Scaffold { padding ->
            Dialog(
                onDismissRequest = {},
            ) {
                AddDialogContent(
                    date = LocalDate.now(),
                    onDateClick = { },
                    weightPickerWidgetState = rememberWeightPickerWidgetState(
                        num = WeightPickerNum(98, 8),
                    ),
                    onWeightChanged = { },
                    onAddClick = { },
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }
}

@Composable
@WeightDropPreview
private fun AddScreenContentEmpty() {
    WeightDropTheme {
        Scaffold { padding ->
            Dialog(
                onDismissRequest = {},
            ) {
                AddDialogContent(
                    date = LocalDate.now().minusWeeks(24),
                    onDateClick = { },
                    weightPickerWidgetState = rememberWeightPickerWidgetState(
                        num = WeightPickerNum(76, 9),
                    ),
                    onWeightChanged = { },
                    onAddClick = { },
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }
}
