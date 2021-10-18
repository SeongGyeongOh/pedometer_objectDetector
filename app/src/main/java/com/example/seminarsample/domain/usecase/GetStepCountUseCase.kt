package com.example.seminarsample.domain.usecase

import com.example.seminarsample.domain.model.StepCountModel
import com.example.seminarsample.domain.repository.local.StepCountRepository
import javax.inject.Inject

class GetStepCountUseCase @Inject constructor(
    private val stepCountRepository: StepCountRepository
): UseCaseWithoutParams<List<StepCountModel>>(){

    public override suspend fun buildUseCase(): List<StepCountModel> {
        return stepCountRepository.getStepCount()
    }
}