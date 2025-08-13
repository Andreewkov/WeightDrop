package ru.andreewkov.weightdrop.route.dialog.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.widget.WeightWheelPickerWidget

@Composable
fun WeightPickerDialogUI(
    weight: Float,
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
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
@WeightDropPreview
private fun WeightPickerDialogUIUIPreview() {
    WeightDropTheme {
        Scaffold { padding ->
            Dialog(
                onDismissRequest = {},
            ) {
                Card(
                    modifier = Modifier.padding(padding),
                    content = { WeightPickerDialogUI(103.2f) },
                )
            }
        }
    }
}
