package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import androidx.annotation.NonNull;
import androidx.work.ForegroundInfo;
import androidx.work.WorkerParameters;
import androidx.work.ListenableWorker.Result;
import androidx.work.Worker;
import com.akira.pisowifitimer.R;
import com.akira.pisowifitimer.StartApplication;
import com.akira.pisowifitimer.pojos.TimeEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TimeWorker extends Worker {
  public static final String KEY_DURATION_HOURS = "duration_hours";
  public static final String KEY_DURATION_MINUTES = "duration_minutes";
  public static final String KEY_DURATION_SECONDS = "duration_seconds";
  public static final int NOTIFICATION_ID = 1;

  private long countdownMillis;
  private final Handler handler = new Handler(Looper.getMainLooper());
  private NotificationManager notificationManager;
  private NotificationCompat.Builder builder;
  private final TimeEvent event = new TimeEvent();
  private boolean isTimerRunning = true;

  public TimeWorker(@NonNull Context context, @NonNull WorkerParameters params) {
    super(context, params);
    int hours = getInputData().getInt(KEY_DURATION_HOURS, 0);
    int minutes = getInputData().getInt(KEY_DURATION_MINUTES, 0);
    int seconds = getInputData().getInt(KEY_DURATION_SECONDS, 0);
    countdownMillis = ((hours * 60 * 60) + (minutes * 60) + seconds) * 1000;
    notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    builder = getNotificationBuilder(context);
  }

  @NonNull
  @Override
  public Result doWork() {
    EventBus.getDefault().register(this);

    startForeground();
    handler.post(
        () ->
            new CountDownTimer(countdownMillis, 1000) {
              public void onTick(long millisUntilFinished) {
                if (isTimerRunning) {
                  builder.setContentText(formatTime(millisUntilFinished));
                  updateNotification(builder);

                  event.setIsBusy(true);
                  EventBus.getDefault().post(event);
                } else {
                  notificationManager.cancel(NOTIFICATION_ID);
                  cancel();
                  
                  event.setIsBusy(false);
                  EventBus.getDefault().post(event);
                }
              }

              public void onFinish() {
                builder.setContentText("Time's up!");
                builder.clearActions();
                builder.setOngoing(false);
                updateNotification(builder);

                event.setIsBusy(false);
                EventBus.getDefault().post(event);
              }
            }.start());

    return Result.success();
  }

  @Override
  public void onStopped() {
    super.onStopped();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe(threadMode = ThreadMode.BACKGROUND)
  public void onTimeEvent(TimeEvent event) {
    if (event.getIsBusy()) isTimerRunning = true;
    else {
      isTimerRunning = false;
    }
  }

  private String formatTime(long millis) {
    int seconds = (int) (millis / 1000) % 60;
    int minutes = (int) ((millis / (1000 * 60)) % 60);
    int hours = (int) ((millis / (1000 * 60 * 60)) % 24);

    return String.format("%02d:%02d:%02d %s", hours, minutes, seconds, "remaining");
  }

  private void startForeground() {
    ForegroundInfo foregroundInfo = new ForegroundInfo(NOTIFICATION_ID, builder.build());
    setForegroundAsync(foregroundInfo);
  }

  private void updateNotification(NotificationCompat.Builder b) {
    notificationManager.notify(NOTIFICATION_ID, b.build());
  }

  private NotificationCompat.Builder getNotificationBuilder(Context context) {
    PendingIntent cancelPendingIntent =
        PendingIntent.getBroadcast(
            context,
            0,
            new Intent(context, TimerBroadcast.class).setAction("CANCEL_WORKER"),
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    return new NotificationCompat.Builder(getApplicationContext(), StartApplication.CHANNEL_ID)
        .setContentTitle("Connected to Wi-Fi")
        .setSmallIcon(R.drawable.ic_alarm)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOngoing(true)
        .setUsesChronometer(true)
        .addAction(R.drawable.ic_alarm, "Cancel", cancelPendingIntent);
  }
}
