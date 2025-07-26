package ru.andreewkov.weightdrop.ui.route.screen.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.ProgressWidgetValue
import ru.andreewkov.weightdrop.ui.WeightChart
import ru.andreewkov.weightdrop.ui.WeightChartCalculator
import ru.andreewkov.weightdrop.ui.theme.Dark
import ru.andreewkov.weightdrop.ui.theme.Grey
import ru.andreewkov.weightdrop.ui.theme.Peach
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.util.isPortrait
import ru.andreewkov.weightdrop.ui.util.stubWeightingsMediumFourth
import ru.andreewkov.weightdrop.ui.widget.ChartWidget
import ru.andreewkov.weightdrop.ui.widget.LoadingIndicatorWidget
import ru.andreewkov.weightdrop.ui.widget.ProgressWidget
import ru.andreewkov.weightdrop.ui.widget.ProgressWidgetColor
import ru.andreewkov.weightdrop.ui.widget.ResultsWidget
import ru.andreewkov.weightdrop.ui.widget.ResultsWidgetItem
import ru.andreewkov.weightdrop.ui.widget.WeightChartColor

@Composable
fun InfoScreenUI() {
    val viewModel: InfoViewModel = hiltViewModel()
    val screenState by viewModel.screenState.get().collectAsState()

    when (val state = screenState) {
        is InfoViewModel.ScreenState.Chart -> {
            InfoScreenContent(
                weightChart = state.weightChart,
            )
        }
        InfoViewModel.ScreenState.Empty -> Unit
        InfoViewModel.ScreenState.Loading -> InfoScreenLoading()
    }
}

@Composable
private fun InfoScreenLoading(
    modifier: Modifier = Modifier,
) {
    LoadingIndicatorWidget(
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier,
    )
}

@Composable
private fun InfoScreenContent(
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
        InfoScreenResults(
            target = weightChart.scope.targetWeight ?: weightChart.scope.bottomWeight,
            max = requireNotNull(maxWeight),
            current = requireNotNull(currentWeight),
        )
        Spacer(modifier = Modifier.size(10.dp))
        InfoScreenChart(weightChart)
    }
}

@Composable
private fun InfoScreenResults(
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
private fun InfoScreenChart(
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
private fun ActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = Peach,
            contentColor = Dark,
            disabledContainerColor = Peach,
            disabledContentColor = Dark,
        ),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = null,
            tint = Dark,
        )
    }
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
private fun InfoScreenContentPreview() {
    val calculator = WeightChartCalculator()
    val target = 86f
    val chart = calculator.calculateWeightChart(target, stubWeightingsMediumFourth)
    WeightDropTheme {
        Scaffold { innerPadding ->
            InfoScreenContent(chart, modifier = Modifier.padding(innerPadding))
        }
    }
}

@WeightDropPreview
@Composable
private fun InfoScreenLoadingPreview() {
    WeightDropTheme {
        Scaffold { innerPadding ->
            InfoScreenLoading(modifier = Modifier.padding(innerPadding))
        }
    }
}
