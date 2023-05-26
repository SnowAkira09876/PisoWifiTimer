package com.akira.pisowifitimer.hilt;

import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.notification.NotifChannelManager;
import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@EntryPoint
@InstallIn(SingletonComponent.class)
public interface AppEntryPoint {

  AkiraRoom getAkiraRoom();

  NotifChannelManager getNotifChannelManager();
  
}
