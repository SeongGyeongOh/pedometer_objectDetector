package com.example.seminarsample.presentation.step

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.seminarsample.utils.Logger

class MyReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Logger.d("브로드캐스트 리시버 - onReceive")

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Logger.d("브로드캐스트 리시버 - ACTION_BOOT_COMPLETED, ACTION_RESTART")

                val workManager = WorkManager.getInstance(context)
                val startServiceRequest = OneTimeWorkRequest.Builder(StepCountWorker::class.java)
                    .build()

                workManager.enqueue(startServiceRequest)
            }

            "ACTION_RESTART" -> {
                val workManager = WorkManager.getInstance(context)
                val startServiceRequest = OneTimeWorkRequest.Builder(StepCountRestartWorker::class.java)
                    .build()

                workManager.enqueue(startServiceRequest)
            }
        }
    }
}
