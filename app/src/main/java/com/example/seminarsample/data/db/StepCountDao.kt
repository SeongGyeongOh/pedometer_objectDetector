package com.example.seminarsample.data.db

import androidx.room.*
import com.example.seminarsample.data.entity.StepCountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepCountDao {
    @Query("SELECT * FROM stepCount_table")
    fun getStepCount(): List<StepCountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stepCountEntity: StepCountEntity) : Long

    @Query("UPDATE stepCount_table SET count = :count WHERE date = :date")
    suspend fun updateStepCount(count: Int, date: String)

    @Query("DELETE FROM stepCount_table WHERE  date = :date")
    suspend fun deleteStepCount(date: String)

    @Query("SELECT * FROM stepCount_table WHERE date = :date")
    fun getTodayCount(date: String) : StepCountEntity?

    @Query("SELECT * FROM stepCount_table WHERE date = :date")
    fun getTodayCountAsFlow(date: String) : Flow<StepCountEntity?>

    @Transaction
    suspend fun upsertStepCount(walkEntity: StepCountEntity) {
        val isExist: StepCountEntity? =
            getTodayCount(walkEntity.date)

        if (isExist == null) {
            insert(walkEntity)
        } else {
            updateStepCount(walkEntity.count, walkEntity.date)
        }
    }
}