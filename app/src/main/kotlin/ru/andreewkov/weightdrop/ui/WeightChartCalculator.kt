package ru.andreewkov.weightdrop.ui

import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.ui.model.WeightPoint
import ru.andreewkov.weightdrop.ui.util.cellToStep
import ru.andreewkov.weightdrop.ui.util.floorToStep
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.max
import kotlin.math.min

data class WeightChart(
    val scope: WeightsScope,
    val weightPoints: List<WeightPoint>,
)

data class WeightsScope(
    val bottomWeight: Float,
    val topWeight: Float,
    val targetWeight: Float?,
    val dividers: List<Float>,
)

class WeightChartCalculator {

    fun calculateWeightChart(targetWeight: Float?, weightings: List<Weighting>): WeightChart {
        check(weightings.isNotEmpty())

        return WeightChart(
            scope = calculateWeightsScope(targetWeight, weightings),
            weightPoints = calculateWeightPoints(weightings),
        )
    }

    private fun calculateWeightsScope(
        targetWeight: Float?,
        weightings: List<Weighting>,
    ): WeightsScope {
        val (minWeight, maxWeight) = weightings.findConfines()
        val minValue = min(minWeight.value, targetWeight ?: minWeight.value)
        val maxValue = max(maxWeight.value, targetWeight ?: maxWeight.value)

        val diff = maxValue - minValue
        val step = getStep(diff)
        val topValue = maxValue.floorToStep(step) + step
        val bottomValue = minValue.cellToStep(step) - step
        val count = ((topValue - bottomValue) / step).toInt() + 1

        return WeightsScope(
            bottomWeight = bottomValue,
            topWeight = topValue,
            targetWeight = targetWeight,
            dividers = List(size = count) { index ->
                bottomValue + step * index
            },
        )
    }

    private fun calculateWeightPoints(weightings: List<Weighting>): List<WeightPoint> {
        val startLocalDate = weightings.first().date
        val endLocalDate = weightings.last().date

        val dayCount = ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt() + 1
        val dividers = findDateDividers(weightings)

        var currentWeightingIndex = 0
        return List(size = dayCount) { index ->
            val date = startLocalDate.plusDays(index.toLong())
            val weighting = weightings[currentWeightingIndex]
                .takeIf { it.date == date }
                .also { if (it != null) currentWeightingIndex++ }

            WeightPoint(
                date = date,
                weightValue = weighting?.value,
                drawDivider = dividers.any { it == date } || index == 0,
            )
        }
    }

    private fun findDateDividers(
        weightings: List<Weighting>,
    ): List<LocalDate> {
        val startDate = weightings.first().date
        val endDate = weightings.last().date

        return when (val days = ChronoUnit.DAYS.between(startDate, endDate) + 1) {
            1L -> listOf(startDate)
            in 2..7 -> buildList {
                add(startDate)
                for (i: Int in 1 until days.toInt() - 1) {
                    add(startDate.plusDays(i.toLong()))
                }
                add(endDate)
            }
            else -> {
                val daysPerDivider = days / 5 + 1
                buildList {
                    var lastDate = endDate
                    while (lastDate.isAfter(startDate) || lastDate == startDate) {
                        add(lastDate)
                        lastDate = lastDate.minusDays(daysPerDivider)
                    }
                }
                buildList {
                    var lastLocalDate = startDate
                    while (lastLocalDate.isBefore(endDate) || lastLocalDate == endDate) {
                        add(lastLocalDate)
                        lastLocalDate = lastLocalDate.plusDays(daysPerDivider)
                    }
                    add(endDate)
                }
            }
        }
    }

    private fun List<Weighting>.findConfines(): Pair<Weighting, Weighting> {
        return minBy { it.value } to maxBy { it.value }
    }

    private fun getStep(diff: Float): Float {
        return when (diff) {
            in 0f..5f -> 0.5f
            in 5f..10f -> 1f
            in 10f..20f -> 2f
            in 20f..55f -> 5f
            in 55f..80f -> 10f
            else -> 25f
        }
    }
}
