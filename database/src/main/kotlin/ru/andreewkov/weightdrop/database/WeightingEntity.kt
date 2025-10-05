package ru.andreewkov.weightdrop.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = WeightingEntity.TABLE_NAME)
data class WeightingEntity(
    @PrimaryKey(autoGenerate = false)
    val date: LocalDate,
    val weight: Float,
) {

    companion object {
        const val TABLE_NAME = "weightings"
        const val DATE_COLUMN = "date"
    }
}
