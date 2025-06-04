package ru.andreewkov.weightdrop.ui

import ru.andreewkov.weightdrop.ui.util.capitalizeFirstChar
import ru.andreewkov.weightdrop.ui.util.hasDecimals
import ru.andreewkov.weightdrop.ui.util.roundToDecimals
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object WeightingFormatter {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM")

    fun formatDate(date: LocalDate): String {
        return date.format(formatter)
    }

    fun formatDateWithDay(date: LocalDate): String {
        val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
        return "${date.format(formatter)} (${dayName.capitalizeFirstChar()}.)"
    }

    fun formatMonthYear(month: Month, year: Int): String {
        val monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
        return "${monthName.capitalizeFirstChar()}, $year"
    }

    fun formatWeightShort(weight: Float): String {
        return if (weight.hasDecimals()) {
            weight.roundToDecimals()
        } else {
            weight.toInt()
        }.toString()
    }


    fun formatWeightLong(weight: Float): String {
        return weight.roundToDecimals().toString()
    }
}

fun main() {
    DayOfWeek.values().forEach {
        println()
    }
}