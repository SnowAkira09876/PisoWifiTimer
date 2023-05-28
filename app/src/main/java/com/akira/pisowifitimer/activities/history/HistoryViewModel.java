package com.akira.pisowifitimer.activities.history;

import androidx.lifecycle.ViewModel;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.pojos.HistoryModel;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public class HistoryViewModel extends ViewModel {
  private HistoryRepo repo;

  public HistoryViewModel(AkiraRoom room) {
    repo = HistoryRepo.getInstance(room);
  }

  public Single<List<HistoryModel>> getHistory() {
    return repo.getHistory();
  }

  public Single<List<HistoryModel>> getHistoryBetween(String startDate, String endDate) {
    return repo.getHistoryBetween(startDate, endDate);
  }
  
  public Single<Integer> getTotalAmountBetween(String startDate, String endDate) {
    return repo.getTotalAmountBetween(startDate, endDate);
  }

  public Single<Integer> getTotalAmount() {
    return repo.getTotalAmount();
  }
}
