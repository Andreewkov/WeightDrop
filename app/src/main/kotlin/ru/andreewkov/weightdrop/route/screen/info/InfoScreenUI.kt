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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.WeightChart
import ru.andreewkov.weightdrop.WeightChartCalculator
import ru.andreewkov.weightdrop.model.ProgressWidgetValue
import ru.andreewkov.weightdrop.theme.Dark
import ru.andreewkov.weightdrop.theme.Grey
import ru.andreewkov.weightdrop.theme.Peach
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.util.isPortrait
import ru.andreewkov.weightdrop.util.stubWeightingsMediumFourth
import ru.andreewkov.weightdrop.widget.ChartWidget
import ru.andreewkov.weightdrop.widget.LoadingIndicatorWidget
import ru.andreewkov.weightdrop.widget.ProgressWidget
import ru.andreewkov.weightdrop.widget.ProgressWidgetColor
import ru.andreewkov.weightdrop.widget.ResultsWidget
import ru.andreewkov.weightdrop.widget.ResultsWidgetItem
import ru.andreewkov.weightdrop.widget.WeightChartColor

@Composable
fun InfoScreenUI() {
    val viewModel: InfoScreenViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsState()

    when (val state = screenState) {
        is InfoScreenState.Chart -> {
            Content(
                weightChart = state.weightChart,
            )
        }
        InfoScreenState.Empty -> Unit
        InfoScreenState.Loading -> Loading()
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier,
) {
    LoadingIndicatorWidget(
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier,
    )
}

@Composable
private fun Content(
    weightChart: WeightChart,
    modifier: Modifier = Modifier,
) {
    val maxWeight by remember {
        derivedStateOf {
            weightChart.weightPoints.maxBy { it.weightValue ?: -1f }.weightValue
        }
    }
    val currentWeight by remember {
        derivedStateOf {
            weightChart.weightPoints.last { it.weightValue != null }.weightValue
        }
    }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        ResultsPanel(
            target = weightChart.scope.targetWeight ?: weightChart.scope.bottomWeight,
            max = requireNotNull(maxWeight),
            current = requireNotNull(currentWeight),
        )
        Spacer(modifier = Modifier.size(10.dp))
        Chart(weightChart)
    }
}

@Composable
private fun ResultsPanel(
    target: Float,
    max: Float,
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
                target = target,
                max = max,
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
    weightChart: WeightChart,
) {
    ChartWidget(
        chart = weightChart,
        color = WeightChartColor(
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
    val calculator = WeightChartCalculator()
    val target = 86f
    val chart = calculator.calculateWeightChart(target, stubWeightingsMediumFourth)
    WeightDropTheme {
        Scaffold { innerPadding ->
            Content(chart, modifier = Modifier.padding(innerPadding))
        }
    }
}

@WeightDropPreview
@Composable
private fun LoadingPreview() {
    WeightDropTheme {
        Scaffold { innerPadding ->
            Loading(modifier = Modifier.padding(innerPadding))
        }
    }
}
