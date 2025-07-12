package ru.andreewkov.weightdrop.ui.screen.dialog

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import ru.andreewkov.weightdrop.ui.screen.AppAction
import ru.andreewkov.weightdrop.ui.screen.AppActionHandler
import ru.andreewkov.weightdrop.ui.screen.Route
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Provider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogUI(
    paramsProvider: Provider<Route.DateDialog.Params>,
    actionHandler: AppActionHandler,
) {
    val state = rememberDatePickerState()
    LaunchedEffect(state.selectedDateMillis) {
        val millis = state.selectedDateMillis
        if (millis != null) {
            val date = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            paramsProvider.get().resultHandler.onDateDialogResult(date)
            actionHandler.handleAction(AppAction.NavigateOnBack)
        }
    }
    DatePickerDialog(
        onDismissRequest = { actionHandler.handleAction(AppAction.NavigateOnBack) },
        confirmButton = { },
        modifier = Modifier.scale(0.95f),
    ) {
        DatePicker(
            state = state,
            showModeToggle = false,
        )
    }
}
