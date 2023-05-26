package com.akira.pisowifitimer.data.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

@Dao
public interface TimeHistoryDAO {

  @Insert
  void insert(TimeHistoryModel model);

  @Update
  void update(TimeHistoryModel model);

  @Delete
  void delete(TimeHistoryModel model);

  @Query("SELECT * FROM TimeHistoryTable ORDER BY id DESC")
  Single<List<TimeHistoryModel>> getTimeHistory();
}
