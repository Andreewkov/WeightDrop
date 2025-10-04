package ru.andreewkov.weightdrop.route.dialog.add

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.WeightingFormatter
import ru.andreewkov.weightdrop.route.dialog.picker.DatePickerDialogUI
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.widget.ButtonContent
import ru.andreewkov.weightdrop.widget.DialogCard
import ru.andreewkov.weightdrop.widget.ValuePanelWidget
import ru.andreewkov.weightdrop.widget.WeightWheelPickerWidget
import java.time.LocalDate

@Composable
fun AddDialogUI(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
) {
    val viewModel: AddDialogViewModel = hiltViewModel()
    val state by viewModel.screenState.collectAsState()
    val datePickerRequestState by viewModel.datePickerRequestState.collectAsState()

    LaunchedEffect(initialDate) {
        viewModel.onDateChanged(initialDate)
    }

    DialogCard(
        onDismissRequest = {
            viewModel.dispose()
            onDismissRequest()
        },
    ) {
        Content(
            date = state.date,
            weight = state.weight,
            onDateClick = viewModel::onDateClick,
            onWeightChanged = viewModel::onWeightChanged,
            onAddClick = {
                viewModel.onWeightAddClick()
                viewModel.dispose()
                onDismissRequest()
            },
        )
    }

    if (datePickerRequestState.show) {
        DatePickerDialogUI(
            initialDate = datePickerRequestState.date,
            titleRes = R.string.dialog_add_date_title,
            onDatePicked = viewModel::onDateChanged,
            onDismissRequest = viewModel::onDatePickerDismissRequest,
        )
    }
}

@Composable
private fun Content(
    date: LocalDate,
    weight: Float,
    onDateClick: (LocalDate) -> Unit,
    onWeightChanged: (Float) -> Unit,
    onAddClick: () -> Unit,
) {
    ButtonContent(
        buttonTextRes = R.string.dialog_add_button,
        onButtonClick = { onAddClick() },
    ) {
        WeightWheelPickerWidget(
            color = Color.White,
            weight = weight,
            requiredHeight = 200.dp,
            onWeightChanged = onWeightChanged,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.size(12.dp))

        ValuePanelWidget(
            title = stringResource(R.string.dialog_add_date_title),
            text = WeightingFormatter.formatDateLong(date),
            tintColor = MaterialTheme.colorScheme.primary,
            iconPainter = painterResource(R.drawable.ic_calendar),
            onClick = {
                onDateClick(date)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
        )
    }
}

@Composable
@WeightDropPreview
private fun ContentPreview() {
    WeightDropTheme {
        ScaffoldPreview {
            Content(
                date = LocalDate.now(),
                weight = 98.8f,
                onDateClick = { },
                onWeightChanged = { },
                onAddClick = { },
            )
        }
    }
}

@Composable
@WeightDropPreview
private fun ContentEmptyPreview() {
    WeightDropTheme {
        ScaffoldPreview {
            Content(
                date = LocalDate.now().minusWeeks(24),
                weight = 76.6f,
                onDateClick = { },
                onWeightChanged = { },
                onAddClick = { },
            )
        }
    }
}
