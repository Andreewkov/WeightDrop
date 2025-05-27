package ru.andreewkov.weightdrop.ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.util.stubWeightingsMediumFourth

@Composable
fun HistoryScreenUI() {
    val viewModel: HistoryViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsState()

    when (val state = screenState) {
        is HistoryViewModel.ScreenState.History -> HistoryScreenContent(state.weightings)
        HistoryViewModel.ScreenState.Loading -> Unit
    }
}

@Composable
fun HistoryScreenContent(
    weightings: List<Weighting>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = weightings) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = it.date.toString())
                Text(text = it.value.toString())
            }
        }
    }
}

@Composable
@WeightDropPreview
fun HistoryScreenContentPreview() {
    WeightDropTheme {
        Scaffold { innerPading ->
            HistoryScreenContent(
                weightings = stubWeightingsMediumFourth,
                modifier = Modifier.padding(innerPading)
            )
        }
    }
}
