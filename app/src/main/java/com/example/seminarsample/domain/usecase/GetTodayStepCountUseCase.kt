package com.example.seminarsample.domain.usecase

import com.example.seminarsample.domain.model.StepCountModel
import com.example.seminarsample.domain.repository.local.StepCountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayStepCountUseCase @Inject constructor(
    private val stepCountRepository: StepCountRepository
) : UseCaseWithParams<Flow<StepCountModel>, String>() {

    public override suspend fun buildUseCase(params: String): Flow<StepCountModel> {
        return stepCountRepository.getTodayCountAsFlow(date = params)
    }
}