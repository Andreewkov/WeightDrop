package ru.andreewkov.weightdrop.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.WeightChart
import ru.andreewkov.weightdrop.WeightChartCalculator
import ru.andreewkov.weightdrop.WeightingFormatter
import ru.andreewkov.weightdrop.model.WeightLineCubicPositions
import ru.andreewkov.weightdrop.model.WeightPoint
import ru.andreewkov.weightdrop.model.WeightPointPosition
import ru.andreewkov.weightdrop.model.WeightPointPositionsScope
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.util.drawHorizontalLine
import ru.andreewkov.weightdrop.util.drawVerticalLine
import ru.andreewkov.weightdrop.util.stubWeightingsMediumFirst
import ru.andreewkov.weightdrop.util.stubWeightingsMediumSecond
import ru.andreewkov.weightdrop.util.stubWeightingsMediumThird
import kotlin.math.abs
import kotlin.math.min

private const val START_MARGIN_CHART = 80f
private const val TOP_MARGIN_CHART = 0f
private const val END_MARGIN_CHART = 0f
private const val BOTTOM_MARGIN_CHART = 60f

data class WeightChartColor(
    val gridColor: Color,
    val textColor: Color,
    val weightLineColor: Color,
    val pointColor: Color,
    val targetLineColor: Color,
)

@Composable
fun ChartWidget(
    chart: WeightChart,
    color: WeightChartColor,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = 12.sp,
        color = color.textColor,
        textAlign = TextAlign.Center,
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drawChartLines(
            color = color.gridColor,
        )

        drawWeightDividers(
            textMeasurer = measurer,
            textStyle = textStyle,
            lineColor = color.gridColor,
            dividers = chart.scope.dividers,
        )

        drawDateDividers(
            textMeasurer = measurer,
            weightPoints = chart.weightPoints,
            textStyle = textStyle,
            lineColor = color.gridColor,
        )

        drawWeightChart(
            chart = chart,
            weightLineColor = color.weightLineColor,
            targetLineColor = color.targetLineColor,
            pointColor = color.pointColor,
        )
    }
}

private fun DrawScope.drawChartLines(color: Color) {
    drawVerticalLine(
        color = color,
        x = 0f,
        strokeWidth = 1f,
    )
    drawHorizontalLine(
        color = color,
        y = size.height,
        strokeWidth = 1f,
    )
}

private fun DrawScope.drawWeightDividers(
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    lineColor: Color,
    dividers: List<Float>,
) {
    inset(
        left = 0f,
        top = TOP_MARGIN_CHART,
        right = 0f,
        bottom = BOTTOM_MARGIN_CHART,
    ) {
        val itemHeight = size.height / (dividers.size - 1)

        dividers.forEachIndexed { index, divider ->
            drawWeightDivider(
                textMeasurer = textMeasurer,
                title = WeightingFormatter.formatWeightShort(divider),
                textStyle = textStyle,
                lineColor = lineColor,
                y = size.height - itemHeight * index,
            )
        }
    }
}

private fun DrawScope.drawWeightDivider(
    textMeasurer: TextMeasurer,
    title: String,
    textStyle: TextStyle,
    lineColor: Color,
    y: Float,
) {
    val textLayoutResult: TextLayoutResult = textMeasurer.measure(
        text = AnnotatedString(title),
        style = textStyle,
    )
    val textSize = textLayoutResult.size
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = START_MARGIN_CHART / 2 - textSize.width / 2,
            y = y - textSize.height / 2,
        ),
    )
    drawHorizontalLine(lineColor, y, startMargin = START_MARGIN_CHART, endMargin = END_MARGIN_CHART)
}

private fun DrawScope.drawDateDividers(
    textMeasurer: TextMeasurer,
    weightPoints: List<WeightPoint>,
    textStyle: TextStyle,
    lineColor: Color,
) {
    inset(
        left = START_MARGIN_CHART,
        top = 0f,
        right = END_MARGIN_CHART,
        bottom = 0f,
    ) {
        val xStep = size.width / (weightPoints.size - 1)

        weightPoints.mapIndexedNotNull { index, point ->
            if (point.drawDivider) {
                index to point.date
            } else {
                null
            }
        }.forEach { (index, date) ->
            val currentX = index * xStep
            drawDateDivider(
                x = currentX,
                title = WeightingFormatter.formatDateShort(date),
                textMeasurer = textMeasurer,
                textStyle = textStyle,
                lineColor = lineColor,
            )
        }
    }
}

private fun DrawScope.drawDateDivider(
    x: Float,
    title: String,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    lineColor: Color,
) {
    drawVerticalLine(lineColor, x, topMargin = TOP_MARGIN_CHART, bottomMargin = BOTTOM_MARGIN_CHART)
    val textLayoutResult: TextLayoutResult = textMeasurer.measure(
        text = AnnotatedString(title),
        style = textStyle,
    )
    val textSize = textLayoutResult.size
    if (x != size.width) {
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = x - textSize.width / 2,
                y = size.height - BOTTOM_MARGIN_CHART / 2 - textSize.height / 2,
            ),
        )
    }
}

private fun DrawScope.drawWeightChart(
    chart: WeightChart,
    weightLineColor: Color,
    targetLineColor: Color,
    pointColor: Color,
) {
    inset(
        left = START_MARGIN_CHART,
        top = TOP_MARGIN_CHART,
        right = END_MARGIN_CHART,
        bottom = BOTTOM_MARGIN_CHART,
    ) {
        val positionsScope = calculatePointPositionsScope(chart)
        val targetY = positionsScope.targetY ?: size.height
        val points = positionsScope.points
        val fillWeightPath = Path()

        fillWeightPath.moveTo(0f, targetY)
        points.firstOrNull()?.let { point ->
            fillWeightPath.lineTo(point.x, point.y)
        }

        drawWeightLines(
            points = points,
            lineColor = weightLineColor,
            fillWeightChart = fillWeightPath::cubicTo,
        )
        fillWeightPath.lineTo(size.width, targetY)

        if (positionsScope.targetY != null) {
            drawHorizontalLine(
                color = targetLineColor,
                y = targetY,
                strokeWidth = 10f,
            )
        }

        drawPath(
            path = fillWeightPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    weightLineColor.copy(alpha = 0.5f),
                    Color.Transparent,
                ),
            ),
        )
        points.forEach { point ->
            drawCircle(
                color = pointColor,
                radius = 8f,
                center = Offset(point.x, point.y),
            )
        }
    }
}

private fun DrawScope.calculatePointPositionsScope(chart: WeightChart): WeightPointPositionsScope {
    val weightPoints = chart.weightPoints
    val scope = chart.scope
    val yStep = size.height / (scope.topWeight - scope.bottomWeight)
    val xStep = size.width / (weightPoints.size - 1)

    return WeightPointPositionsScope(
        points = weightPoints.mapIndexedNotNull { index, weightPoint ->
            weightPoint.weightValue?.let { weight ->
                WeightPointPosition(
                    x = index * xStep,
                    y = yStep * (scope.topWeight - weight),
                    weightPoint = weightPoint,
                )
            }
        },
        targetY = scope.targetWeight?.let { (scope.topWeight - scope.targetWeight) * yStep },
    )
}

private fun DrawScope.drawWeightLines(
    points: List<WeightPointPosition>,
    lineColor: Color,
    fillWeightChart: (WeightLineCubicPositions) -> Unit,
) {
    val lines = points.zipWithNext()

    lines.forEachIndexed { index, (startPoint, endPoint) ->
        val prevPoint = if (index > 0) lines[index - 1].first else startPoint
        val nextPoint = if (index < lines.lastIndex) lines[index + 1].second else endPoint
        val cubicPositions = findLineCubicPositions(prevPoint, startPoint, endPoint, nextPoint)

        drawPath(
            path = Path().apply {
                moveTo(cubicPositions.startX, cubicPositions.startY)
                cubicTo(cubicPositions)
            },
            color = lineColor,
            style = Stroke(
                width = 3.dp.toPx(),
            ),
        )
        fillWeightChart(cubicPositions)
    }
}

private fun findLineCubicPositions(
    prev: WeightPointPosition,
    start: WeightPointPosition,
    end: WeightPointPosition,
    next: WeightPointPosition,
): WeightLineCubicPositions {
    val prevLineDiffY = start.y - prev.y
    val prevLineDiffX = start.x - prev.x

    val currentLineDiffY = end.y - start.y
    val currentLineDiffX = end.x - start.x

    val nextLineDiffY = next.y - end.y
    val nextLineDiffX = next.x - end.x

    val isPrevUp = prevLineDiffY < 0
    val isCurrentUp = currentLineDiffY < 0
    val isNextUp = nextLineDiffY < 0

    val (startRefractionX, startRefractionY) = when {
        prevLineDiffX == 0f -> {
            start.x to start.y
        }
        isCurrentUp != isPrevUp -> {
            (start.x + 0.5f * min(abs(prevLineDiffX), abs(currentLineDiffX))) to start.y
        }
        else -> {
            val diffX = min(prevLineDiffX, currentLineDiffX)
            val diffY = min(abs(prevLineDiffY), abs(currentLineDiffY))
            val yFactor = if (isCurrentUp) -2 else 2
            start.x + diffX / 3 to start.y + diffY / yFactor
        }
    }

    val (endRefractionX, endRefractionY) = when {
        nextLineDiffX == 0f -> {
            end.x to end.y
        }
        isCurrentUp != isNextUp -> {
            (end.x - 0.5f * min(abs(currentLineDiffX), abs(nextLineDiffX))) to end.y
        }
        else -> {
            val diffX = min(currentLineDiffX, nextLineDiffX)
            val diffY = min(abs(currentLineDiffY), abs(nextLineDiffY))
            val yFactor = if (isCurrentUp) 2 else -2
            end.x - diffX / 3 to end.y + diffY / yFactor
        }
    }

    return WeightLineCubicPositions(
        startX = start.x,
        startY = start.y,
        startRefractionX = startRefractionX,
        startRefractionY = startRefractionY,
        endRefractionX = endRefractionX,
        endRefractionY = endRefractionY,
        endX = end.x,
        endY = end.y,
    )
}

private fun Path.cubicTo(positions: WeightLineCubicPositions): Path {
    return this.also {
        cubicTo(
            x1 = positions.startRefractionX,
            y1 = positions.startRefractionY,
            x2 = positions.endRefractionX,
            y2 = positions.endRefractionY,
            x3 = positions.endX,
            y3 = positions.endY,
        )
    }
}

@WeightDropPreview
@Composable
private fun ChartWidgetPreview() {
    val calculator = WeightChartCalculator()
    val target = 90f
    WeightDropTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                ChartWidget(
                    chart = calculator.calculateWeightChart(target, stubWeightingsMediumFirst),
                    color = WeightChartColor(
                        gridColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary,
                        weightLineColor = MaterialTheme.colorScheme.secondary,
                        pointColor = MaterialTheme.colorScheme.tertiary,
                        targetLineColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                )
            }
        }
    }
}

@WeightDropPreview
@Composable
private fun ChartWidgetPreview2() {
    val calculator = WeightChartCalculator()
    val target = 80f
    WeightDropTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                ChartWidget(
                    chart = calculator.calculateWeightChart(target, stubWeightingsMediumSecond),
                    color = WeightChartColor(
                        gridColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary,
                        weightLineColor = MaterialTheme.colorScheme.secondary,
                        pointColor = MaterialTheme.colorScheme.tertiary,
                        targetLineColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                )
            }
        }
    }
}

@WeightDropPreview
@Composable
private fun ChartWidgetPreview3() {
    val calculator = WeightChartCalculator()
    val target = 88f
    WeightDropTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                ChartWidget(
                    chart = calculator.calculateWeightChart(target, stubWeightingsMediumThird),
                    color = WeightChartColor(
                        gridColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary,
                        weightLineColor = MaterialTheme.colorScheme.secondary,
                        pointColor = MaterialTheme.colorScheme.tertiary,
                        targetLineColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                )
            }
        }
    }
}
