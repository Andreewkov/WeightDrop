package ru.andreewkov.weightdrop.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity
data class WeightingDBO(
    @PrimaryKey
    val date: LocalDate,
    val weight: Float,
)