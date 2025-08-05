package ru.andreewkov.weightdrop.route.dialog.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.WeightingFormatter
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.widget.ValuePanelWidget
import ru.andreewkov.weightdrop.widget.WeightWheelPickerWidget
import java.time.LocalDate

@Composable
fun AddDialogUI(
    date: LocalDate?,
    onDateClick: (LocalDate) -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: AddDialogViewModel = hiltViewModel()
    val state by viewModel.screenState.collectAsState()

    LaunchedEffect(date) {
        viewModel.setDate(date ?: LocalDate.now())
    }

    Card(
        modifier = modifier,
    ) {
        Content(
            date = state.date,
            weight = state.weight,
            onDateClick = onDateClick,
            onWeightChanged = viewModel::onWeightChanged,
            onAddClick = {
                viewModel.onWeightAddClick()
                onButtonClick()
            },
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
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = 32.dp,
                vertical = 16.dp,
            ),
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
            title = stringResource(R.string.screen_add_date_title),
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
                text = stringResource(R.string.dialog_add_button),
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
private fun ContentPreview() {
    WeightDropTheme {
        Scaffold { padding ->
            Dialog(
                onDismissRequest = {},
            ) {
                Card(
                    modifier = Modifier.padding(padding),
                ) {
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
    }
}

@Composable
@WeightDropPreview
private fun ContentEmptyPreview() {
    WeightDropTheme {
        Scaffold { padding ->
            Dialog(
                onDismissRequest = {},
            ) {
                Card(
                    modifier = Modifier.padding(padding),
                ) {
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
    }
}
