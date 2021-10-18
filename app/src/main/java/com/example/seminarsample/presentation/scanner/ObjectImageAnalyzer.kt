package com.example.seminarsample.presentation.scanner

import android.annotation.SuppressLint
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.seminarsample.utils.GraphicOverlay
import com.example.seminarsample.utils.Logger
import com.example.seminarsample.utils.convertYuv420888ImageToBitmap
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import kotlinx.coroutines.FlowPreview

class ObjectImageAnalyzer(
    private val graphicOverlay: GraphicOverlay,
    localModel: LocalModel,
    private val objLabel: String,
    val callback: (Pair<ObjectDetector, String>) -> Unit

) : ImageAnalysis.Analyzer {

    private val objOptions = CustomObjectDetectorOptions.Builder(localModel)
        .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
        .setClassificationConfidenceThreshold(0.5f)
        .setMaxPerObjectLabelCount(3)
        .enableClassification()
        .build()

    private val detector: ObjectDetector = ObjectDetection.getClient(objOptions)

    @FlowPreview
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image

        mediaImage?.let { it ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees

            val convertImageToBitmap = convertYuv420888ImageToBitmap(mediaImage)

            val inputImage = InputImage.fromBitmap(convertImageToBitmap, rotationDegrees)

            detector.process(inputImage)
                .addOnSuccessListener { objects ->
                    graphicOverlay.clear()
                    for (obj in objects) {
                        if(!obj.labels.isNullOrEmpty()) {
                            val label = obj.labels[0]
                            callback(Pair(detector, label.text))
                            Logger.d("사물 인식 label ${label.text}")
                            drawGraphicOverlay(mediaImage, obj, label.text)
                        }

                        Logger.d("사물 인식 labelList: ${obj.labels}")
                        Logger.d("사물 인식 trackingId: ${obj.trackingId}")
                    }
                }
                .addOnFailureListener {
                    Logger.d("사물 인식 실패 : $it")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                    mediaImage.close()
                }
        }
    }

    private fun drawGraphicOverlay(mediaImage: Image, obj: DetectedObject, label: String) {
        mediaImage.let {
            val barcodeGraphic = ObjectGraphic(graphicOverlay, obj, label, it.cropRect)
            graphicOverlay.add(barcodeGraphic)
        }
        graphicOverlay.postInvalidate()
    }
}