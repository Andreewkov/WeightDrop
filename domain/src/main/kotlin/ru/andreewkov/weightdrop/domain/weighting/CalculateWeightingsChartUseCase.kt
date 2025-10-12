package ru.andreewkov.weightdrop.domain.weighting

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.domain.di.ChartDispatcherQualifier
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.model.WeightingsChart
import ru.andreewkov.weightdrop.domain.model.WeightingsChartScope
import ru.andreewkov.weightdrop.utils.cellToStep
import ru.andreewkov.weightdrop.utils.floorToStep
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min
import kotlin.ranges.contains

class CalculateWeightingsChartUseCase @Inject constructor(
    @ChartDispatcherQualifier private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(
        targetWeight: Float?,
        weightings: List<Weighting>,
    ): Result<WeightingsChart> = withContext(dispatcher) {
        runCatching {
            check(weightings.isNotEmpty())

            WeightingsChart(
                scope = calculateScope(targetWeight, weightings),
                weightings = weightings,
            )
        }
    }

    private fun calculateScope(
        targetWeight: Float?,
        weightings: List<Weighting>,
    ): WeightingsChartScope {
        val sortWeightings = weightings.sortedBy { it.date }
        val (minWeight, maxWeight) = weightings.findConfines()
        val minValue = min(minWeight.value, targetWeight ?: minWeight.value)
        val maxValue = max(maxWeight.value, targetWeight ?: maxWeight.value)

        val diff = maxValue - minValue
        val step = getStep(diff)
        val topValue = maxValue.floorToStep(step) + step
        val bottomValue = minValue.cellToStep(step) - step
        val count = ((topValue - bottomValue) / step).toInt() + 1

        return WeightingsChartScope(
            bottomWeight = bottomValue,
            topWeight = topValue,
            targetWeight = targetWeight,
            startWeighting = sortWeightings.first(),
            endWeighting = sortWeightings.last(),
            dividers = List(size = count) { index ->
                bottomValue + step * index
            },
            dateDividers = findDateDividers(weightings),
        )
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
