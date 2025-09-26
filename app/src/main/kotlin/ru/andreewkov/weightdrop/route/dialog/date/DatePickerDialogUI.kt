package ru.andreewkov.weightdrop.route.dialog.date

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
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.util.drawHorizontalWheelLines
import ru.andreewkov.weightdrop.widget.ButtonContent
import ru.andreewkov.weightdrop.widget.DateWheelPickerWidget
import ru.andreewkov.weightdrop.widget.DialogCard
import java.time.LocalDate

@Composable
fun DatePickerDialogUI(
    initialDate: LocalDate,
    onDateChanged: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
) {
    DialogCard(
        onDismissRequest = onDismissRequest,
    ) {
        Content(
            initialDate = initialDate,
            onButtonClick = { date ->
                onDateChanged(date)
                onDismissRequest()
            }
        )
    }
}

@Composable
private fun Content(
    initialDate: LocalDate,
    onButtonClick: (LocalDate) -> Unit,
) {
    var currentDate by remember { mutableStateOf(initialDate) }

    ButtonContent(
        titleRes = R.string.dialog_date_picker_title,
        buttonTextRes = R.string.dialog_date_picker_button,
        onButtonClick = {
            onButtonClick(currentDate)
        },
        isButtonEnabled = { !currentDate.isAfter(LocalDate.now()) }
    ) {
        DateWheelPickerWidget(
            date = currentDate,
            color = Color.White, // TODO
            onDateChanged = { currentDate = it },
            requiredHeight = 200.dp,
            modifier = Modifier
                .height(200.dp)
                .drawHorizontalWheelLines(7)
                .padding(horizontal = 12.dp),
        )
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
                    colors = cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    )
                ) {
                    Content(
                        initialDate = LocalDate.now(),
                        onButtonClick = { },
                    )
                }
            }
        }
    }
}
