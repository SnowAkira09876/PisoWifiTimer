package com.akira.pisowifitimer.activities.stats;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.akira.pisowifitimer.data.room.AkiraRoom;

public class StatsViewModelFactory implements ViewModelProvider.Factory {
  private AkiraRoom room;
  
  public StatsViewModelFactory(AkiraRoom room) {
    this.room = room;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(StatsViewModel.class)) {
      return modelClass.cast(new StatsViewModel(room));
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
