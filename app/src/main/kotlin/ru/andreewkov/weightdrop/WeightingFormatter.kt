package ru.andreewkov.weightdrop

import ru.andreewkov.weightdrop.util.capitalizeFirstChar
import ru.andreewkov.weightdrop.util.hasDecimals
import ru.andreewkov.weightdrop.util.roundToDecimals
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object WeightingFormatter {

    private val shortDateFormatter = DateTimeFormatter.ofPattern("dd.MM")
    private val longDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    fun formatDateShort(date: LocalDate): String {
        return date.format(shortDateFormatter)
    }

    fun formatDateLong(date: LocalDate): String {
        return date.format(longDateFormatter)
    }

    fun formatDateShortWithDay(date: LocalDate): String {
        val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
        return "${date.format(shortDateFormatter)} (${dayName.capitalizeFirstChar()}.)"
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
