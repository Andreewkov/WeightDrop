package ru.andreewkov.weightdrop.ui.screen.dialog

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogUI(
    onDismissRequest: () -> Unit,
    onConfirmClick: (LocalDate) -> Unit,
) {
    val state = rememberDatePickerState()
    LaunchedEffect(state.selectedDateMillis) {
        val millis = state.selectedDateMillis
        if (millis != null) {
            val date = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            onConfirmClick(date)
        }
    }
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = { },
        modifier = Modifier.scale(0.95f),
    ) {
        DatePicker(
            state = state,
            showModeToggle = false,
        )
    }
}
