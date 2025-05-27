package ru.andreewkov.weightdrop.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDate

@ProvidedTypeConverter
class LocalDateTypeConverter {

    @TypeConverter
    fun localDateToLong(localDate: LocalDate): Long {
        return localDate.toEpochDay()
    }

    @TypeConverter
    fun longToLocalDate(long: Long): LocalDate {
        return LocalDate.ofEpochDay(long)
    }
}
