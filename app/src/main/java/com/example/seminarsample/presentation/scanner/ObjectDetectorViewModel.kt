package com.example.seminarsample.presentation.scanner

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.*
import com.example.seminarsample.utils.Logger
import com.example.seminarsample.utils.throttleFirst
import com.google.mlkit.vision.barcode.Barcode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObjectDetectorViewModel @Inject constructor(

): ViewModel(){

    private var _state = MutableStateFlow<ObjectDetectorState>(ObjectDetectorState.Idle)
    val state: StateFlow<ObjectDetectorState>
        get() = _state

    private var _intent = MutableSharedFlow<ObjectDetectorIntent>()
    val intent: SharedFlow<ObjectDetectorIntent>
        get() = _intent

    private val _processCameraProvider =  MutableLiveData<ProcessCameraProvider>()
    val processCameraProvider: LiveData<ProcessCameraProvider> get() = _processCameraProvider

    private val _objLabel =  MutableSharedFlow<String>()
    val objLabel: LiveData<String> get() = _objLabel.throttleFirst(3000).asLiveData()

    init {
        handleIntent()
    }

    fun setIntent(intent: ObjectDetectorIntent) {
        viewModelScope.launch { _intent.emit(intent) }
    }

    private fun handleIntent() = viewModelScope.launch {
        intent.collect {
            when (it) {
                ObjectDetectorIntent.Init -> cameraListen()
                is ObjectDetectorIntent.SetCamera -> setCamera(it.cameraProvider)
                is ObjectDetectorIntent.SetObjectDetector -> {
                    objectDetectorDebounce(it.detectedObj)
                }
            }
        }
    }

    private fun cameraListen() {
        _state.value = try {
            ObjectDetectorState.SetupCamera
        } catch (e: Exception) {
            ObjectDetectorState.Fail(Error("카메라 세팅에 실패했습니다", e.cause))
        }
    }

    private fun setCamera(provider: ProcessCameraProvider) = viewModelScope.launch {
        Logger.d("뷰모델: setCamera")
        _processCameraProvider.value = provider
        _state.value = ObjectDetectorState.DetectObject
    }

    private fun objectDetectorDebounce(detectedObj: String) {
        viewModelScope.launch {
            _objLabel.emit(detectedObj)
        }
    }
}