package com.akira.pisowifitimer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.akira.pisowifitimer.feature_timer.presentation.TimerWorker.Companion.ALARM
import com.akira.pisowifitimer.feature_timer.presentation.TimerWorker.Companion.TIMER
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class StartApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(TIMER, "Timer", "Long time worker for countdown timer")
        createNotificationChannel(ALARM, "Alarm", "Notification when timer finished")
    }

    private fun createNotificationChannel(id: String?, name: String?, description: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
    }
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}