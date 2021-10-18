package com.example.seminarsample.presentation.scanner

import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.objects.DetectedObject

sealed class ObjectDetectorIntent {
    object Init : ObjectDetectorIntent()
    data class SetCamera(val cameraProvider: ProcessCameraProvider) : ObjectDetectorIntent()
    data class SetObjectDetector(val detectedObj: String) : ObjectDetectorIntent()
}