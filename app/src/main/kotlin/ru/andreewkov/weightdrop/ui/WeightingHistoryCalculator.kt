package ru.andreewkov.weightdrop.ui

import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.ui.model.WeightingHistoryItem

object WeightingHistoryCalculator {

    fun calculate(
        weightings: List<Weighting>,
        previous: Weighting? = null,
    ): List<WeightingHistoryItem> {
        return buildList {
            weightings
                .groupBy { it.date.year }
                .map {
                    it.value.groupBy { it.date.month }.toSortedMap()
                }
                .forEach {
                    it.values.forEach { weightings ->
                        val firstItem = weightings.firstOrNull()
                        if (firstItem != null) {
                            add(
                                WeightingHistoryItem(
                                    header = WeightingHistoryItem.Header(
                                        month = firstItem.date.month,
                                        year = firstItem.date.year
                                    ).takeIf { previous?.date?.month != firstItem.date.month },
                                    weightings = weightings,
                                )
                            )
                        }
                    }
                }
        }
    }
}
