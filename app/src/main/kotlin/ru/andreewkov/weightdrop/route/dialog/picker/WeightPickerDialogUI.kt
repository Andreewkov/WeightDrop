package ru.andreewkov.weightdrop.route.dialog.picker

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.widget.ButtonContent
import ru.andreewkov.weightdrop.widget.DialogCard
import ru.andreewkov.weightdrop.widget.WeightWheelPickerWidget

@Composable
fun WeightPickerDialogUI(
    initialWeight: Float,
    @StringRes titleRes: Int,
    onWeightPicked: (Float) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var currentWeight by remember { mutableFloatStateOf(initialWeight) }

    DialogCard(
        onDismissRequest = onDismissRequest,
    ) {
        ButtonContent(
            titleRes = titleRes,
            buttonTextRes = R.string.dialog_height_picker_button,
            onButtonClick = {
                onWeightPicked(currentWeight)
                onDismissRequest()
            },
        ) {
            WeightWheelPickerWidget(
                color = Color.White,
                weight = currentWeight,
                requiredHeight = 200.dp,
                onWeightChanged = { weight ->
                    currentWeight = weight
                },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
@WeightDropPreview
private fun WeightPickerDialogUIUIPreview() {
    WeightDropTheme {
        ScaffoldPreview {
            WeightPickerDialogUI(
                initialWeight = 103.6f,
                titleRes = R.string.settings_target_weight_title,
                onWeightPicked = { },
                onDismissRequest = { },
            )
        }
    }
}
