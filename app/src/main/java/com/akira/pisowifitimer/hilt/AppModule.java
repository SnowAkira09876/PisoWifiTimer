package com.akira.pisowifitimer.hilt;

import android.content.Context;
import androidx.room.Room;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

  @Provides
  public static AkiraRoom provideAkiraRoom(@ApplicationContext Context context) {
    return Room.databaseBuilder(context, AkiraRoom.class, "AkiraRoom").build();
  }

  @Provides
  public static ExecutorService provideExecutorService() {
    return Executors.newSingleThreadExecutor();
  }
}
