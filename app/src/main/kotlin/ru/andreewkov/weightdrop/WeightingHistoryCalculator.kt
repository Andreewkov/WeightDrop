package ru.andreewkov.weightdrop

import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.model.WeightingHistory
import ru.andreewkov.weightdrop.util.roundToDecimals
import ru.andreewkov.weightdrop.util.stubWeightingsMediumThird
import java.time.LocalDate

object WeightingHistoryCalculator {

    fun calculate(
        weightings: List<Weighting>,
        previous: Weighting? = null,
    ): List<WeightingHistory> {
        check(weightings.isNotEmpty())
        val sortedWeightings = weightings.sortedBy { it.date }
        var prevWeight = sortedWeightings.first().value

        val historyItems = sortedWeightings.map { weighting ->
            WeightingHistory.Item(
                weighting = weighting,
                diff = (weighting.value - prevWeight).roundToDecimals(),
            ).also {
                prevWeight = weighting.value
            }
        }

        return buildList {
            var startIndex = 0
            var monthDate = sortedWeightings.first().date
            historyItems.forEachIndexed { index, item ->
                val currentDate = item.weighting.date
                if (currentDate.month != monthDate.month || currentDate.year != monthDate.year) {
                    addHeader(
                        monthDate = monthDate,
                        items = historyItems.subList(startIndex, index),
                        takeHeader = startIndex != 0 || previous?.date?.month != monthDate.month,
                    )

                    monthDate = item.weighting.date
                    startIndex = index
                }
            }

            addHeader(
                monthDate = monthDate,
                items = historyItems.subList(startIndex, historyItems.size),
                takeHeader = startIndex != 0 || previous?.date?.month != monthDate.month,
            )
        }.reversed()
    }

    private fun MutableList<WeightingHistory>.addHeader(
        monthDate: LocalDate,
        items: List<WeightingHistory.Item>,
        takeHeader: Boolean,
    ) {
        add(
            WeightingHistory(
                header = WeightingHistory.Header(
                    month = monthDate.month,
                    year = monthDate.year,
                    diff = items.map { it.diff }.sum().roundToDecimals(),
                ).takeIf { takeHeader },
                weightingItems = items.reversed(),
            ),
        )
    }
}

fun main() {
    val t3 = System.currentTimeMillis()
    val result2 = WeightingHistoryCalculator.calculate(stubWeightingsMediumThird)
    result2.forEach {
        println(it.header)
        it.weightingItems.forEach {
            println(it)
        }
    }
    val t4 = System.currentTimeMillis()
    println("seconds: " + (t4 - t3))
}
