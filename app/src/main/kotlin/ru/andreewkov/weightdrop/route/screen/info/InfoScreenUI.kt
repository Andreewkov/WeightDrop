package ru.andreewkov.weightdrop.route.screen.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.WeightingsChart
import ru.andreewkov.weightdrop.domain.weighting.CalculateWeightingsChartUseCase
import ru.andreewkov.weightdrop.model.ProgressWidgetValue
import ru.andreewkov.weightdrop.model.WeightingsChartColor
import ru.andreewkov.weightdrop.route.screen.LoadingScreenUI
import ru.andreewkov.weightdrop.theme.Dark
import ru.andreewkov.weightdrop.theme.Grey
import ru.andreewkov.weightdrop.theme.Peach
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.util.isPortrait
import ru.andreewkov.weightdrop.util.stubWeightingsMediumFourth
import ru.andreewkov.weightdrop.widget.ChartWidget
import ru.andreewkov.weightdrop.widget.ProgressPanelWidget
import ru.andreewkov.weightdrop.widget.ProgressPanelWidgetState
import ru.andreewkov.weightdrop.widget.ProgressWidget
import ru.andreewkov.weightdrop.widget.ProgressWidgetColor
import ru.andreewkov.weightdrop.widget.ResultsWidget
import ru.andreewkov.weightdrop.widget.ResultsWidgetItem

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
private fun ResultsPanel(
    start: Float,
    target: Float,
    current: Float,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(getContentHeight()),
    ) {
        ProgressWidget(
            color = ProgressWidgetColor(
                inactiveColor = Peach,
                shadowColor = Grey,
                positivePrimaryColor = Color(0xFF5CDE2A),
                positiveSecondaryColor = Color(0xFF266C0E),
                negativePrimaryColor = Color(0xFFFC643B),
                negativeSecondaryColor = Color(0xFFAD2E0D),
            ),
            value = ProgressWidgetValue(
                start = start,
                target = target,
                current = current,
            ),
            modifier = Modifier.fillMaxHeight(),
        )

        Spacer(modifier = Modifier.size(20.dp))

        if (isPortrait()) {
            ButtonsWeightPortraitContent()
        } else {
            ButtonsWeightLandscapeContent()
        }
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

context(RowScope)
@Composable
private fun ButtonsWeightPortraitContent() {
    Column {
        ResultsWidget(
            results = createResultsWidgetItems(),
            background = Peach,
            textColor = Dark,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.size(6.dp))
    }
}

context(RowScope)
@Composable
private fun ButtonsWeightLandscapeContent() {
    ResultsWidget(
        results = createResultsWidgetItems(),
        background = Peach,
        textColor = Dark,
        modifier = Modifier.width(300.dp),
    )
    Spacer(modifier = Modifier.size(6.dp))
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier,
    ) {
    }
}

private fun createResultsWidgetItems(): List<ResultsWidgetItem> {
    return listOf(
        ResultsWidgetItem("Начальный вес", 94.4f),
        ResultsWidgetItem("Максимальный вес", 94.4f),
        ResultsWidgetItem("Текущий вес", 87f),
        ResultsWidgetItem("Процент успеха", 15.4f),
    )
}

@Composable
private fun getContentHeight(): Dp {
    return if (isPortrait()) {
        120.dp
    } else {
        80.dp
    }
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
