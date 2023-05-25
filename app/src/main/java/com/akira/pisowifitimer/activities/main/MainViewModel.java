package com.akira.pisowifitimer.activities.main;

import androidx.lifecycle.ViewModel;
import com.akira.pisowifitimer.data.wifi.WifiData;
import io.reactivex.rxjava3.core.Observable;

public class MainViewModel extends ViewModel {
  private WifiData wifiData;
  private MainRepo repo;

  public MainViewModel(WifiData wifiData) {
    repo = new MainRepo(wifiData);
  }

  public Observable<String> observableWifiGateway() {
    return repo.observableWifiGateway();
  }

  public void registerNetworkCallback() {
    repo.registerNetworkCallback();
  }

  public void unregisterNetworkCallback() {
    repo.unregisterNetworkCallback();
  }
}
