package com.example.seminarsample.presentation.step

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seminarsample.domain.usecase.GetStepCountUseCase
import com.example.seminarsample.domain.usecase.GetTodayStepCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepCountViewModel @Inject constructor(
    private val getStepCountUseCase: GetStepCountUseCase,
    private val getTodayStepCountUseCase: GetTodayStepCountUseCase
) : ViewModel() {

    private val _stepCountIntent = MutableSharedFlow<StepCountIntent>()
    val stepCountIntent: SharedFlow<StepCountIntent> get() = _stepCountIntent

    private val _stepCountState = MutableStateFlow<StepCountState>(StepCountState.Idle)
    val stepCountState: StateFlow<StepCountState> get() = _stepCountState

    private val _stepCount = MutableLiveData<Int>(0)
    val stepCount: LiveData<Int> get() = _stepCount

    init {
        handleIntent()
    }

    fun setIntent(intent: StepCountIntent) = viewModelScope.launch {
        _stepCountIntent.emit(intent)
    }

    fun countSteps(count: Int) {
        _stepCount.value = count
    }

    private fun handleIntent() = viewModelScope.launch {
        stepCountIntent.collect {
            when (it) {
                StepCountIntent.CountWalk -> {
                    _stepCountState.value = StepCountState.Counting
                }
                StepCountIntent.GetData -> {
                    getStepCountData()
                }
                is StepCountIntent.GetTodayData -> {
                    getTodayStepCountData(it.date)
                }
            }
        }
    }

    private fun getStepCountData() = viewModelScope.launch(Dispatchers.IO) {
        _stepCountState.value = try {
            StepCountState.TotalCount(getStepCountUseCase.buildUseCase())
        } catch (e: Exception) {
            StepCountState.Fail(Error("만보기 데이터 호출에 실패했습니다", e.cause))
        }
    }

    private fun getTodayStepCountData(date: String) = viewModelScope.launch(Dispatchers.IO) {
        _stepCountState.value = try {
            StepCountState.TodayCount(getTodayStepCountUseCase.buildUseCase(date))
        } catch (e: Exception) {
            StepCountState.Fail(Error("오늘의 만보기 데이터 호출에 실패했습니다", e.cause))
        }
    }
}