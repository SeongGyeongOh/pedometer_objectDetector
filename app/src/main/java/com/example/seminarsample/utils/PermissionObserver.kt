package com.example.seminarsample.utils

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PermissionObserver(
    private val registry: ActivityResultRegistry
) : DefaultLifecycleObserver {

    lateinit var camera: ActivityResultLauncher<String>
    lateinit var cameraCallback: (Boolean) -> Unit

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = "0x80"
    }

    override fun onCreate(owner: LifecycleOwner) {
        camera = registry.register(
            REQUEST_CAMERA_PERMISSION,
            owner,
            ActivityResultContracts.RequestPermission()
        ) {
            cameraCallback(it)
        }
    }

    public fun requestCameraPermission(callback: (Boolean) -> Unit) {
        cameraCallback = callback
        camera.launch(android.Manifest.permission.CAMERA)
    }
}