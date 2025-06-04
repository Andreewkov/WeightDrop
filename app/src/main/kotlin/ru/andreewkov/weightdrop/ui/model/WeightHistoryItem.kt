package ru.andreewkov.weightdrop.ui.model

import ru.andreewkov.weightdrop.domain.model.Weighting
import java.time.Month

data class WeightingHistoryItem(
    val header: Header?,
    val weightings: List<Weighting>,
) {

    data class Header(
        val month: Month,
        val year: Int,
    )
}