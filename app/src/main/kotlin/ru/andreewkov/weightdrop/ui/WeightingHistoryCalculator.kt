package ru.andreewkov.weightdrop.ui

import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.ui.model.WeightingHistoryItem

object WeightingHistoryCalculator {

    fun calculate(
        weightings: List<Weighting>,
        previous: Weighting? = null,
    ): List<WeightingHistoryItem> {
        check(weightings.isNotEmpty())
        val weightings = weightings.sortedByDescending { it.date }

        return buildList {
            var startIndex = 0
            var monthDate = weightings.first().date

            weightings.forEachIndexed { index, weighting ->
                val currentDate = weighting.date
                if (currentDate.month != monthDate.month || currentDate.year != monthDate.year) {
                    add(
                        WeightingHistoryItem(
                            header = WeightingHistoryItem.Header(
                                month = monthDate.month,
                                year = monthDate.year,
                                diff = weightings[startIndex].value - weighting.value,
                            ).takeIf { startIndex != 0 || previous?.date?.month != monthDate.month },
                            weightings = weightings.subList(startIndex, index),
                        )
                    )

                    monthDate = weighting.date
                    startIndex = index
                }
            }

            add(
                WeightingHistoryItem(
                    header = WeightingHistoryItem.Header(
                        month = monthDate.month,
                        year = monthDate.year,
                        diff = weightings[startIndex].value - weightings.last().value,
                    ).takeIf { startIndex != 0 || previous?.date?.month != monthDate.month },
                    weightings = weightings.subList(startIndex, weightings.size),
                )
            )
        }
    }
}
