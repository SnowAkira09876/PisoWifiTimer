package com.akira.pisowifitimer.activities.history;

import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.pojos.HistoryModel;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public class HistoryRepo {
  private static volatile HistoryRepo instance;
  private AkiraRoom room;

  public HistoryRepo(AkiraRoom room) {
    this.room = room;
  }

  public static HistoryRepo getInstance(AkiraRoom room) {
    if (instance == null) {
      synchronized (HistoryRepo.class) {
        if (instance == null) {
          instance = new HistoryRepo(room);
        }
      }
    }
    return instance;
  }

  public Single<List<HistoryModel>> getHistory() {
    return room.getHistoryDAO().getHistory();
  }

  public Single<List<HistoryModel>> getHistoryBetween(String startDate, String endDate) {
    return room.getHistoryDAO().getHistoryBetween(startDate, endDate);
  }
  
  public Single<Integer> getTotalAmount() {
    return room.getHistoryDAO().getTotalAmount();
  }

  public Single<Integer> getTotalAmountBetween(String startDate, String endDate) {
    return room.getHistoryDAO().getTotalAmountBetween(startDate, endDate);
  }
  
  public Single<String> getStartDate() {
    return room.getHistoryDAO().getStartDate();
  }
  
  public Single<String> getEndDate() {
    return room.getHistoryDAO().getEndDate();
  }
}
