package ru.andreewkov.weightdrop.ui.model

import ru.andreewkov.weightdrop.domain.model.Weighting
import java.time.Month

data class WeightingHistory(
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
