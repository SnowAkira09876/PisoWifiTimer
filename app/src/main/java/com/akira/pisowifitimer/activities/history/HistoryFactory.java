package com.akira.pisowifitimer.activities.history;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.akira.pisowifitimer.data.room.AkiraRoom;

public class HistoryFactory implements ViewModelProvider.Factory {
  private AkiraRoom room;
  
  public HistoryFactory(AkiraRoom room) {
    this.room = room;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(HistoryViewModel.class)) {
      return modelClass.cast(new HistoryViewModel(room));
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
