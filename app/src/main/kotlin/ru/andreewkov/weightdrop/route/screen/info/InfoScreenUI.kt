package ru.andreewkov.weightdrop.route.screen.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.WeightingsChart
import ru.andreewkov.weightdrop.domain.weighting.CalculateWeightingsChartUseCase
import ru.andreewkov.weightdrop.model.WeightingsChartColor
import ru.andreewkov.weightdrop.route.screen.LoadingScreenUI
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.util.stubWeightingsMediumFourth
import ru.andreewkov.weightdrop.widget.ChartWidget
import ru.andreewkov.weightdrop.widget.ProgressPanelWidget
import ru.andreewkov.weightdrop.widget.ProgressPanelWidgetState

@Composable
fun InfoScreenUI() {
    val viewModel: InfoScreenViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsState()

    when (val state = screenState) {
        InfoScreenState.Loading -> {
            LoadingScreenUI()
        }
        is InfoScreenState.SuccessChart -> {
            Content(
                state = state,
            )
        }
        InfoScreenState.SuccessEmpty -> Unit
        InfoScreenState.Failure -> Unit
    }
}

@Composable
private fun Content(
    state: InfoScreenState.SuccessChart,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
//        ResultsPanel(
//            start = chart.scope.startWeighting.value,
//            target = chart.scope.targetWeight ?: chart.scope.bottomWeight,
//            current = chart.scope.endWeighting.value,
//        )
        ProgressPanelWidget(
            primaryColor = MaterialTheme.colorScheme.secondary,
            secondaryColor = MaterialTheme.colorScheme.tertiary,
            state = ProgressPanelWidgetState(
                startWeight = state.settings.startWeight,
                currentWeight = state.chart.scope.endWeighting.value,
                targetWeight = state.settings.targetWeight,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        )
        Spacer(modifier = Modifier.size(16.dp))
        Chart(state.chart)
    }
}

@Composable
private fun Chart(
    chart: WeightingsChart,
) {
    ChartWidget(
        chart = chart,
        color = WeightingsChartColor(
            gridColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.primary,
            weightLineColor = MaterialTheme.colorScheme.secondary,
            pointColor = MaterialTheme.colorScheme.tertiary,
            targetLineColor = MaterialTheme.colorScheme.tertiary,
        ),
    )
}

@WeightDropPreview
@Composable
private fun ContentPreview() {
    val target = 86f
    val chart = runBlocking {
        CalculateWeightingsChartUseCase(Dispatchers.Default).invoke(target, stubWeightingsMediumFourth)
    }.getOrThrow()
    WeightDropTheme {
        ScaffoldPreview {
            Content(InfoScreenState.SuccessChart(chart, Settings(null, null, null, null)))
        }
    }
}
