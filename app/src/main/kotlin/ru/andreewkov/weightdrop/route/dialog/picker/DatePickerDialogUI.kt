package ru.andreewkov.weightdrop.route.dialog.picker

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.util.drawHorizontalWheelLines
import ru.andreewkov.weightdrop.widget.ButtonContent
import ru.andreewkov.weightdrop.widget.DateWheelPickerWidget
import ru.andreewkov.weightdrop.widget.DialogCard
import java.time.LocalDate

@Composable
fun DatePickerDialogUI(
    initialDate: LocalDate,
    @StringRes titleRes: Int,
    onDatePicked: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var currentDate by remember { mutableStateOf(initialDate) }

    DialogCard(
        onDismissRequest = onDismissRequest,
    ) {
        ButtonContent(
            titleRes = titleRes,
            buttonTextRes = R.string.dialog_date_picker_button,
            onButtonClick = {
                onDatePicked(currentDate)
                onDismissRequest()
            },
            isButtonEnabled = { !currentDate.isAfter(LocalDate.now()) },
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
}

@Composable
@WeightDropPreview
private fun ContentPreview() {
    WeightDropTheme {
        ScaffoldPreview {
            DatePickerDialogUI(
                initialDate = LocalDate.now(),
                titleRes = R.string.settings_start_date_title,
                onDatePicked = { },
                onDismissRequest = { },
            )
        }
    }
}
