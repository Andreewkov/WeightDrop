package ru.andreewkov.weightdrop.ui.screen.dialog

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.screen.AppAction
import ru.andreewkov.weightdrop.ui.screen.AppActionHandler
import ru.andreewkov.weightdrop.ui.screen.Route
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.widget.DateWheelPickerWidget
import java.time.LocalDate
import javax.inject.Provider

@Composable
fun DatePickerDialogUI(
    paramsProvider: Provider<Route.DateDialog.Params>,
    actionHandler: AppActionHandler,
) {
    Card {
        DatePickerDialogContent(
            date = paramsProvider.get().date,
            onButtonClick = { date ->
                paramsProvider.get().resultHandler.onDateDialogResult(date)
                actionHandler.handleAction(AppAction.NavigateOnBack)
            },
        )
    }
}

@Composable
private fun DatePickerDialogContent(
    date: LocalDate,
    onButtonClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentDate by remember { mutableStateOf(date) }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = 32.dp,
                vertical = 16.dp,
            ),
    ) {
        DateWheelPickerWidget(
            date = date,
            color = Color.White, // TODO
            onDateChanged = { currentDate = it },
            requiredHeight = 140.dp,
            modifier = Modifier
                .height(140.dp)
                .padding(horizontal = 12.dp),
        )

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            onClick = { onButtonClick(currentDate) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
            modifier = Modifier
                .height(42.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.dialog_date_picker_button),
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
private fun DatePickerDialogContentPreview() {
    WeightDropTheme {
        Scaffold { padding ->
            Dialog(
                onDismissRequest = {},
            ) {
                DatePickerDialogContent(
                    date = LocalDate.now(),
                    onButtonClick = { },
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }
}
