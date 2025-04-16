package ru.andreewkov.weightdrop.ui.screen.add

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import java.time.LocalDate

@Composable
fun AddScreenUI() {
    val viewModel: AddViewModel = hiltViewModel()
    val state by viewModel.screenState.collectAsState()

    AddScreenContent(state.date, state.weight)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddScreenContent(
    date: LocalDate,
    weight: Float?,
    modifier: Modifier = Modifier,
) {
    val datePickerState = rememberDatePickerState()
    DatePicker(
        state = datePickerState,
        showModeToggle = false
    )
}

@Composable
@WeightDropPreview
private fun AddScreenContentPreview() {
    WeightDropTheme {
        Scaffold { padding ->
            AddScreenContent(
                date = LocalDate.now(),
                weight = 103.2f,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
@WeightDropPreview
private fun AddScreenContentEmpty() {
    WeightDropTheme {
        Scaffold { padding ->
            AddScreenContent(
                date = LocalDate.now().minusWeeks(24),
                weight = null,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
