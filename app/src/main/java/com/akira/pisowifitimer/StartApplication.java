package com.akira.pisowifitimer;

import android.app.Application;
import com.akira.pisowifitimer.notification.ChannelKeys;
import com.akira.pisowifitimer.notification.NotifChannelManager;
import com.akira.pisowifitimer.pojos.TimeItemModel;
import dagger.hilt.android.HiltAndroidApp;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;

@HiltAndroidApp
public class StartApplication extends Application {
  @Inject NotifChannelManager notif;

  @Override
  public void onCreate() {
    super.onCreate();

    notif.createNotificationChannel(
        ChannelKeys.TIMER.valueOf(), "Timer", "Long time worker for countdown timer");
    notif.createNotificationChannel(
        ChannelKeys.ALARM.valueOf(), "Alarm", "Notification when timer finished");

    EventBus.builder()
        .logNoSubscriberMessages(false)
        .sendNoSubscriberEvent(false)
        .addIndex(new TimeItemModel())
        .installDefaultEventBus();
  }
}
