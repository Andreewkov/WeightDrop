package ru.andreewkov.weightdrop.domain.model

import java.time.Month

data class HistoryBlock(
    val header: Header?,
    val weightingItems: List<Item>,
) {

    data class Item(
        val weighting: Weighting,
        val diff: Float,
    )

    data class Header(
        val month: Month,
        val year: Int,
        val diff: Float,
    )
}
