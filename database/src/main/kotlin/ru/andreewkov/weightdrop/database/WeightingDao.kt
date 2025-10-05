package ru.andreewkov.weightdrop.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WeightingDao {

    @Query("SELECT * FROM ${WeightingEntity.TABLE_NAME} ORDER BY ${WeightingEntity.DATE_COLUMN}")
    fun getWeightings(): Flow<List<WeightingEntity>>

    @Query("SELECT * FROM ${WeightingEntity.TABLE_NAME} WHERE ${WeightingEntity.DATE_COLUMN} LIKE :date")
    fun getWeighting(date: LocalDate): WeightingEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeighting(entity: WeightingEntity)

    @Delete
    fun deleteWeighting(entity: WeightingEntity)
}
