package com.akira.pisowifitimer.feature_timer.presentation

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.akira.pisowifitimer.R
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import com.akira.pisowifitimer.feature_timer.domain.repository.TimerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


@HiltWorker
class TimerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationManager: NotificationManager,
    private val repository: TimerRepository
) : CoroutineWorker(appContext, workerParams) {
    private val timer by lazy {
        notificationBuilder(
            title = TIMER, channel = TIMER
        )
    }
    private val alarm by lazy {
        notificationBuilder(
            title = ALARM, channel = ALARM
        )
    }

    private val hours by lazy {
        inputData.getInt(HOUR, 0)
    }

    private val minutes by lazy {
        inputData.getInt(MINUTE, 0)
    }

    private val seconds by lazy {
        inputData.getInt(SECOND, 0)
    }

    private val date by lazy {
        inputData.getString(DATE)
    }

    private val time by lazy {
        inputData.getString(TIME)
    }

    private val amount by lazy {
        inputData.getString(AMOUNT)
    }

    private val _timerId = 0
    private val _alarmId = 1

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val countdownMillis =
                hours * TimeUnit.HOURS.toMillis(1) + minutes * TimeUnit.MINUTES.toMillis(1) + seconds * TimeUnit.SECONDS.toMillis(
                    1
                )

            val startTimeMillis = System.currentTimeMillis()
            while (true) {
                val remainingMillis =
                    countdownMillis - (System.currentTimeMillis() - startTimeMillis)

                val remainingHours = TimeUnit.MILLISECONDS.toHours(remainingMillis)
                val remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
                val remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60

                val countdownText =
                    "${remainingHours}h:${remainingMinutes}m:${remainingSeconds}s remaining"

                timer.setContentText(countdownText)
                notificationManager.notify(_timerId, timer.build())

                delay(1000)

                if (remainingMillis <= 0) {
                    alarm.setContentText("Countdown Timer finished!")
                    alarm.setOngoing(false)
                    notificationManager.notify(_alarmId, alarm.build())
                    notificationManager.cancel(_timerId)

                    val timeInfo = TimeInfo(time = time, date = date, amount = amount)
                    repository.insertHistory(timeInfo)
                    break
                }
            }

            return@withContext Result.success()
        }
    }

    private fun notificationBuilder(
        title: String, channel: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channel).setContentTitle(title)
            .setSmallIcon(R.drawable.ic_alarm)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setOngoing(true).setShowWhen(true)
    }

    companion object {
        const val HOUR = "hour"
        const val MINUTE = "minute"
        const val SECOND = "second"

        const val DATE = "date"
        const val TIME = "time"
        const val AMOUNT = "amount"

        const val TIMER = "Timer"
        const val ALARM = "Alarm"
    }
}