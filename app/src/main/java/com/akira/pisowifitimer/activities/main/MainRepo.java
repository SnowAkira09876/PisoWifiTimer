package com.akira.pisowifitimer.activities.main;

import com.akira.pisowifitimer.data.wifi.WifiData;
import io.reactivex.rxjava3.core.Observable;

public class MainRepo {
  private WifiData wifiData;

  public MainRepo(WifiData wifiData) {
    this.wifiData = wifiData;
  }

  public Observable<String> observableWifiGateway() {
    return wifiData.observableWifiGateway();
  }

  public void registerNetworkCallback() {
    wifiData.registerNetworkCallback();
  }

  public void unregisterNetworkCallback() {
    wifiData.unregisterNetworkCallback();
  }
}
