package com.example.seminarsample.presentation.step

import com.example.seminarsample.domain.model.StepCountModel
import kotlinx.coroutines.flow.Flow

sealed class StepCountState {
    object Idle : StepCountState()
    object Counting : StepCountState()
    data class TotalCount(val walkData: List<StepCountModel>) : StepCountState()
    data class TodayCount(val walkData: Flow<StepCountModel>) : StepCountState()
    data class Fail(val error: Error) : StepCountState()
}