package com.akira.pisowifitimer.activities.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.akira.pisowifitimer.data.wifi.WifiData;

public class MainViewModelFactory implements ViewModelProvider.Factory {
  private WifiData wifiData;

  public MainViewModelFactory(WifiData wifiData) {
    this.wifiData = wifiData;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(MainViewModel.class)) {
      return modelClass.cast(new MainViewModel(wifiData));
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
