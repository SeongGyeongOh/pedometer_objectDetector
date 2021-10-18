package com.example.seminarsample.presentation.step

sealed class StepCountIntent {
    object CountWalk : StepCountIntent()
    object GetData : StepCountIntent()
    data class GetTodayData(val date: String) : StepCountIntent()
}