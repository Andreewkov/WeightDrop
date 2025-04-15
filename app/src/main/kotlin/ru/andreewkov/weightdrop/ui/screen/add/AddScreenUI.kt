package ru.andreewkov.weightdrop.ui.screen.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import java.time.LocalDate

@Composable
fun AddScreenUI() {
    val viewModel: AddViewModel = viewModel()
    val state by viewModel.screenState.collectAsState()

    AddScreenContent(state.date, state.weight)
}

@Composable
private fun AddScreenContent(
    date: LocalDate,
    weight: Float?,
) {

}

@Composable
@WeightDropPreview
private fun AddScreenContentPreview() {
    WeightDropTheme {
        AddScreenContent(
            date = LocalDate.now(),
            weight = 103.2f,
        )
    }
}

@Composable
@WeightDropPreview
private fun AddScreenContentEmpty() {
    WeightDropTheme {
        AddScreenContent(
            date = LocalDate.now().minusWeeks(24),
            weight = null,
        )
    }
}
