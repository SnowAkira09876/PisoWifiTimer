package com.akira.pisowifitimer.activities.stats;

import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public class StatsRepo {
  private static volatile StatsRepo instance;
  private AkiraRoom room;

  public static StatsRepo getInstance(AkiraRoom room) {
    if (instance == null) {
      synchronized (StatsRepo.class) {
        if (instance == null) {
          instance = new StatsRepo(room);
        }
      }
    }
    return instance;
  }

  public StatsRepo(AkiraRoom room) {
    this.room = room;
  }

  public Single<List<TimeHistoryModel>> getTimeHistory() {
    return room.getTimeHistoryDAO().getTimeHistory();
  }
}
