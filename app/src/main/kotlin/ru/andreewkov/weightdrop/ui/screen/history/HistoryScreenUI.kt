package ru.andreewkov.weightdrop.ui.screen.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.widget.WeightPickerNum
import ru.andreewkov.weightdrop.ui.widget.WeightPickerWidget
import ru.andreewkov.weightdrop.ui.widget.rememberWeightPickerWidgetState

@Composable
fun HistoryScreenUI() {
    HistoryScreenContent()
}

@Composable
fun HistoryScreenContent(
    modifier: Modifier = Modifier,
) {
    val state = rememberWeightPickerWidgetState(
        num = WeightPickerNum(
            integer = 0,
            fraction = 0,
        )
    )
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        WeightPickerWidget(
            state = state,
            primaryColor = MaterialTheme.colorScheme.primary,
            secondaryColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.height(300.dp),
        )
        Spacer(modifier = Modifier.size(100.dp))
        Button(
            onClick = {
                state.updateValue(WeightPickerNum(76, 2))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Click me!"
            )
        }
    }

}

@Composable
@WeightDropPreview
fun HistoryScreenContentPreview() {
    WeightDropTheme {
        Scaffold { innerPading ->
            HistoryScreenContent(
                modifier = Modifier.padding(innerPading)
            )
        }
    }
}
