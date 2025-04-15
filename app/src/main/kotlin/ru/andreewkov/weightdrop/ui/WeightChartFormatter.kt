package ru.andreewkov.weightdrop.ui

import ru.andreewkov.weightdrop.ui.util.hasDecimals
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object WeightChartFormatter {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM")

    fun formatDate(date: LocalDate): String {
        return date.format(formatter)
    }

    fun formatWeightShort(weight: Float): String {
        return if (weight.hasDecimals()) {
            weight
        } else {
            weight.toInt()
        }.toString()
    }


    fun formatWeightLong(weight: Float): String {
        return weight.toString()
    }
}