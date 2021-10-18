package com.example.seminarsample.presentation.step

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.seminarsample.utils.Logger
import com.example.seminarsample.utils.Pref

class StepCountWorker(
    val context: Context,
    parameters: WorkerParameters
) : Worker(context, parameters) {

    var pref = Pref(context)

    override fun doWork(): Result {
        Logger.d("워커1 실행")
        Logger.d("워커 isServiceRunning ${pref.getBoolVal("isServiceRunning")} \n needWorker ${pref.getBoolVal("needWorker")}")
        if (pref.getBoolVal("isServiceRunning") && pref.getBoolVal("needWorker")) {
            val intent = Intent(context, StepCountService::class.java)
            intent.putExtra("isReboot", true)
            ContextCompat.startForegroundService(context, intent)
        }
        return Result.success()
    }
}