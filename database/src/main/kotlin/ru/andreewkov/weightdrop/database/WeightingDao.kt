package ru.andreewkov.weightdrop.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightingDao {

    @Query("SELECT * FROM weightingdbo ORDER BY date")
    fun getWeightings(): Flow<List<WeightingDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeighting(dbo: WeightingDBO)

    @Delete
    fun deleteWeighting(dbo: WeightingDBO)
}
