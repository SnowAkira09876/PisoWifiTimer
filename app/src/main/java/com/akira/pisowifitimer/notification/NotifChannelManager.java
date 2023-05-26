package com.akira.pisowifitimer.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.akira.pisowifitimer.StartApplication;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import com.akira.pisowifitimer.R;

public class NotifChannelManager {
  private NotificationManager manager;
  private Context context;

  @Inject
  public NotifChannelManager(@ApplicationContext Context context) {
    this.context = context;
    manager = context.getSystemService(NotificationManager.class);
  }

  public void createNotificationChannel(String id, String name, String description) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(id, name, importance);
      channel.setDescription(description);
      manager.createNotificationChannel(channel);
    }
  }

  public NotificationCompat.Builder getNotificationBuilder(String title, String channel) {
    return new NotificationCompat.Builder(context, channel)
        .setContentTitle(title)
        .setSmallIcon(R.drawable.ic_alarm)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOngoing(true)
        .setUsesChronometer(true);
  }

  public void updateNotification(NotificationCompat.Builder b, int notifId) {
    manager.notify(notifId, b.build());
  }
}
