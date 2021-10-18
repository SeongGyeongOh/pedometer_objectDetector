package com.example.seminarsample.domain.repository.local

import com.example.seminarsample.domain.model.StepCountModel
import kotlinx.coroutines.flow.Flow

interface StepCountRepository {
    suspend fun getStepCount(): List<StepCountModel>
    suspend fun deleteStepCount(date: String)
    suspend fun getTodayCountAsFlow(date: String): Flow<StepCountModel>
    suspend fun getTodayCount(date: String): StepCountModel
    suspend fun updateStepCount(date: String, count: Int)
    suspend fun upsertStepCount(stepCountData: StepCountModel)
}