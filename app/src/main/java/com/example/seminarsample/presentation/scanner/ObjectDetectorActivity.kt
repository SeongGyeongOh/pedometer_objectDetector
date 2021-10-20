package com.example.seminarsample.presentation.scanner

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.example.seminarsample.databinding.ActivityObjectDetectorBinding
import com.example.seminarsample.utils.Logger
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.objects.ObjectDetector
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class ObjectDetectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityObjectDetectorBinding
    private val viewModel: ObjectDetectorViewModel by viewModels()
    private var preview: Preview? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var objectDetector: ObjectDetector? = null
    private val cameraExecutor: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityObjectDetectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPreview()
        initData()
        bindCamera()
        requestIntent()
        handleState()
    }

    private fun requestIntent() {
        viewModel.setIntent(ObjectDetectorIntent.Init)
    }

    private fun handleState() {
        viewModel.state.asLiveData().observe(this) {
            when (it) {
                ObjectDetectorState.Idle -> { }

                ObjectDetectorState.SetupCamera -> {
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
                    cameraProviderFuture.addListener({
                        Logger.d("addListener")

                        viewModel.setIntent(ObjectDetectorIntent.SetCamera(cameraProviderFuture.get())) },
                        ContextCompat.getMainExecutor(this))
                }

                ObjectDetectorState.DetectObject -> {
                    Logger.d("오브젝트 액티비티 detectObject")
                    startCamera()
                }
            }
        }
    }

    private fun initData() {
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }

    private fun startCamera() {
        viewModel.processCameraProvider.observe(this) {
            cameraProvider = it
            bindCamera()
        }
    }

    private fun bindCamera() {
        Logger.d("오브젝트 액티비티 detectObject")
        cameraProvider?.let { provider ->
            provider.unbindAll()
            setPreview()
            analyzer()
        }
    }

    private fun setPreview() {
        preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
    }

    var label: String = ""
    private fun getObjectLabel() {
        viewModel.objLabel.observe(this) {
            label = it
            viewModel.setIntent(ObjectDetectorIntent.SetObjectDetector(it))
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun analyzer() {
        val localModel = LocalModel.Builder()
//            .setAssetFilePath("object_labeler.tflite")
            .setAssetFilePath("model.tflite")
            .build()

        imageAnalysis = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, ObjectImageAnalyzer(
                    binding.graphicOverlay,
                    localModel,
                    label
                ){ (objectDetector, detectecdObject) ->
                    if (this.objectDetector == null) {
                        this.objectDetector = objectDetector
                    }
//                    viewModel.setIntent(ObjectDetectorIntent.SetObjectDetector(detectecdObject))
                })
            }

        if (cameraProvider != null && cameraSelector != null) {
            cameraProvider!!.bindToLifecycle(this, cameraSelector!!, preview, imageAnalysis)
        }
    }
}