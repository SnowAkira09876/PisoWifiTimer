package com.akira.pisowifitimer.data.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.akira.pisowifitimer.pojos.HistoryModel;

@Database(
    entities = {HistoryModel.class},
    exportSchema = false,
    version = 1)
public abstract class AkiraRoom extends RoomDatabase {
  public abstract HistoryDAO getHistoryDAO();
}
