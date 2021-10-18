package com.example.seminarsample.utils

import android.content.Context
import android.preference.PreferenceManager
import androidx.annotation.StringRes
import com.example.seminarsample.R
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase.DetectorMode
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
    require(periodMillis > 0) { "period should be positive" }
    return flow {
        var lastTime = 0L
        collect { value ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= periodMillis) {
                lastTime = currentTime
                emit(value)
            }
        }
    }
}

private fun getObjectDetectorOptions(
    context: Context,
    @StringRes prefKeyForMultipleObjects: Int,
    @StringRes prefKeyForClassification: Int,
    @DetectorMode mode: Int
): ObjectDetectorOptions? {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val enableMultipleObjects =
        sharedPreferences.getBoolean(context.getString(prefKeyForMultipleObjects), false)
    val enableClassification =
        sharedPreferences.getBoolean(context.getString(prefKeyForClassification), true)
    val builder = ObjectDetectorOptions.Builder().setDetectorMode(mode)
    if (enableMultipleObjects) {
        builder.enableMultipleObjects()
    }
    if (enableClassification) {
        builder.enableClassification()
    }
    return builder.build()
}

fun getObjectDetectorOptionsForLivePreview(context: Context): ObjectDetectorOptions? {
    return getObjectDetectorOptions(
        context,
        R.string.pref_key_live_preview_object_detector_enable_multiple_objects,
        R.string.pref_key_live_preview_object_detector_enable_classification,
        ObjectDetectorOptions.STREAM_MODE
    )
}

private fun getCustomObjectDetectorOptions(
    context: Context,
    localModel: LocalModel,
    @StringRes prefKeyForMultipleObjects: Int,
    @StringRes prefKeyForClassification: Int,
    @DetectorMode mode: Int
): CustomObjectDetectorOptions? {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val enableMultipleObjects =
        sharedPreferences.getBoolean(context.getString(prefKeyForMultipleObjects), false)
    val enableClassification =
        sharedPreferences.getBoolean(context.getString(prefKeyForClassification), true)
    val builder = CustomObjectDetectorOptions.Builder(localModel).setDetectorMode(mode)
    if (enableMultipleObjects) {
        builder.enableMultipleObjects()
    }
    if (enableClassification) {
        builder.enableClassification().setMaxPerObjectLabelCount(1)
    }
    return builder.build()
}

fun getCustomObjectDetectorOptionsForLivePreview(
    context: Context, localModel: LocalModel
): CustomObjectDetectorOptions? {
    return getCustomObjectDetectorOptions(
        context,
        localModel,
        R.string.pref_key_live_preview_object_detector_enable_multiple_objects,
        R.string.pref_key_live_preview_object_detector_enable_classification,
        CustomObjectDetectorOptions.STREAM_MODE
    )
}
