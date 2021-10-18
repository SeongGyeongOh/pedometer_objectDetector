package com.example.seminarsample.data.repository.local

import com.example.seminarsample.data.db.StepCountDao
import com.example.architecturekotlin.data.mapper.map
import com.example.seminarsample.domain.model.StepCountModel
import com.example.seminarsample.domain.repository.local.StepCountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StepCountRepositoryImpl @Inject constructor(
    private val stepCountDao: StepCountDao
) : StepCountRepository {

    override suspend fun getStepCount(): List<StepCountModel> {
        return stepCountDao.getStepCount().map {
            it.map()
        }
    }

    override suspend fun deleteStepCount(date: String) {
        stepCountDao.deleteStepCount(date)
    }

    override suspend fun getTodayCountAsFlow(date: String): Flow<StepCountModel> {
        return stepCountDao.getTodayCountAsFlow(date).map {
            it?.map() ?: StepCountModel()
        }
    }

    override suspend fun getTodayCount(date: String): StepCountModel {
        return stepCountDao.getTodayCount(date)?.map() ?: StepCountModel()
    }

    override suspend fun updateStepCount(date: String, count: Int) {
        return stepCountDao.updateStepCount(count, date)
    }

    override suspend fun upsertStepCount(walkData: StepCountModel) {
        return stepCountDao.upsertStepCount(walkData.map())
    }
}