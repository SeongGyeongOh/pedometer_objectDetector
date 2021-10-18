package com.example.seminarsample.presentation.step

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.seminarsample.utils.Logger
import com.example.seminarsample.utils.Pref

class StepCountRestartWorker constructor(
    val context: Context,
    parameters: WorkerParameters
) : Worker(context, parameters) {

    var pref = Pref(context)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        Logger.d("워커2 실행")

        if (pref.getBoolVal("isServiceRunning") && pref.getBoolVal("needWorker")) {
            val intent = Intent(context, StepCountService::class.java)
            intent.putExtra("isRestart", true)
            ContextCompat.startForegroundService(context, intent)
        }

        return Result.success()
    }
}