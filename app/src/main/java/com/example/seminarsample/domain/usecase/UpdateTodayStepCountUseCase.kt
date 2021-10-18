package com.example.seminarsample.domain.usecase

import com.example.seminarsample.domain.model.StepCountModel
import com.example.seminarsample.domain.repository.local.StepCountRepository
import javax.inject.Inject

class UpdateTodayStepCountUseCase @Inject constructor(
    private val repository: StepCountRepository
) : UseCaseWithParams<Unit, StepCountModel>(){

    public override suspend fun buildUseCase(params: StepCountModel) {
        return repository.upsertStepCount(stepCountData = params)
    }
}