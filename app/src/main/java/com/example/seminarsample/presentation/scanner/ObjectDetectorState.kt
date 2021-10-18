package com.example.seminarsample.presentation.scanner

sealed class ObjectDetectorState {
    object Idle : ObjectDetectorState()
    object SetupCamera : ObjectDetectorState()
    object DetectObject : ObjectDetectorState()
    data class Fail(val error: Error) : ObjectDetectorState()
}