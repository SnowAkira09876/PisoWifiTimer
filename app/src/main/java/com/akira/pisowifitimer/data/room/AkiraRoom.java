package com.akira.pisowifitimer.data.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;

@Database(
    entities = {TimeHistoryModel.class},
    exportSchema = false,
    version = 1)
public abstract class AkiraRoom extends RoomDatabase {
  public abstract TimeHistoryDAO getTimeHistoryDAO();
}
