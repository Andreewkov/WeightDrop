package ru.andreewkov.weightdrop.ui.screen.add

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import ru.andreewkov.weightdrop.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDatePickerDialog(
    onDismissRequest: () -> Unit,
) {
    val state = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = { ConfirmButton(onDismissRequest) },
        properties = DialogProperties(usePlatformDefaultWidth = true),
        modifier = Modifier.scale(0.9f)
    ) {
        DatePicker(
            state = state,
        )
    }
}

@Composable
private fun ConfirmButton(onClick: ()-> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.add_dialog_add_button),
            color = MaterialTheme.colorScheme.surface,
            style = TextStyle(
                fontSize = 20.sp,
            )
        )
    }
}
