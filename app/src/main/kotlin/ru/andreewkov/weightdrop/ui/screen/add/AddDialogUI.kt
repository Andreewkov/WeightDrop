package ru.andreewkov.weightdrop.ui.screen.add

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
import ru.andreewkov.weightdrop.ui.WeightingFormatter
import ru.andreewkov.weightdrop.ui.screen.AppAction
import ru.andreewkov.weightdrop.ui.screen.AppActionHandler
import ru.andreewkov.weightdrop.ui.screen.Route
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.widget.ValuePanelWidget
import ru.andreewkov.weightdrop.ui.widget.WeightWheelPickerWidget
import java.time.LocalDate
import javax.inject.Provider

@Composable
fun AddDialogUI(
    paramsProvider: Provider<Route.AddDialog.Params>,
    actionHandler: AppActionHandler,
    modifier: Modifier = Modifier,
) {
    val viewModel: AddViewModel = hiltViewModel()
    val state by viewModel.screenState.get().collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setInitialDate(paramsProvider.get().date)
    }

    Card {
        AddDialogContent(
            date = state.date,
            weight = state.weight,
            onDateClick = { date ->
                actionHandler.handleAction(
                    AppAction.NavigateToRoute(
                        route = Route.DateDialog(
                            params = Route.DateDialog.Params(
                                date = date,
                                resultHandler = viewModel,
                            ),
                        ),
                    ),
                )
            },
            onWeightChanged = viewModel::onWeightChanged,
            onAddClick = {
                viewModel.onWeightAddClick()
                actionHandler.handleAction(AppAction.NavigateOnBack)
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun AddDialogContent(
    date: LocalDate,
    weight: Float,
    onDateClick: (LocalDate) -> Unit,
    onWeightChanged: (Float) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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
private fun AddSDialogContentPreview() {
    WeightDropTheme {
        Scaffold { padding ->
            Dialog(
                onDismissRequest = {},
            ) {
                AddDialogContent(
                    date = LocalDate.now(),
                    weight = 98.8f,
                    onDateClick = { },
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
private fun AddDialogContentEmpty() {
    WeightDropTheme {
        Scaffold { padding ->
            Dialog(
                onDismissRequest = {},
            ) {
                AddDialogContent(
                    date = LocalDate.now().minusWeeks(24),
                    weight = 76.6f,
                    onDateClick = { },
                    onWeightChanged = { },
                    onAddClick = { },
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }
}
