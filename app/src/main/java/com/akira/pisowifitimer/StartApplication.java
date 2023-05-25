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
  public static final String CHANNEL_ID = "countdown_channel";

  @Override
  public void onCreate() {
    super.onCreate();
    createNotificationChannel();

    EventBus.builder()
        .logNoSubscriberMessages(false)
        .sendNoSubscriberEvent(false)
        .addIndex(new TimeItemModel())
        .installDefaultEventBus();
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.akira_notification_channel);
      String description = getString(R.string.akira_notification_channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
      channel.setDescription(description);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
}
