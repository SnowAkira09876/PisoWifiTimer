package com.akira.pisowifitimer.activities.stats;

import androidx.lifecycle.ViewModel;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public class StatsViewModel extends ViewModel{
  private StatsRepo repo; 
  
  public StatsViewModel(AkiraRoom room) {
    repo = StatsRepo.getInstance(room);
  }

  public Single<List<TimeHistoryModel>> getTimeHistory() {
    return repo.getTimeHistory();
  }
  
}
