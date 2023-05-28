package com.akira.pisowifitimer;

import android.app.Application;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.os.Build;
import com.akira.pisowifitimer.pojos.TimeItemModel;
import dagger.hilt.android.HiltAndroidApp;
import org.greenrobot.eventbus.EventBus;

@HiltAndroidApp
public class StartApplication extends Application {
  public static final String TIMER = "timer", ALARM = "alarm";

  @Override
  public void onCreate() {
    super.onCreate();

    createNotificationChannel(TIMER, "Timer", "Long time worker for countdown timer");
    createNotificationChannel(ALARM, "Alarm", "Notification when timer finished");

    EventBus.builder()
        .logNoSubscriberMessages(false)
        .sendNoSubscriberEvent(false)
        .addIndex(new TimeItemModel())
        .installDefaultEventBus();
  }

  public void createNotificationChannel(String id, String name, String description) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(id, name, importance);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);

      channel.setDescription(description);
      notificationManager.createNotificationChannel(channel);
    }
  }
}
