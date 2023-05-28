package com.akira.pisowifitimer.activities.main;

import com.akira.pisowifitimer.data.wifi.WifiData;
import io.reactivex.rxjava3.core.Observable;

public class MainRepo {
  private WifiData wifiData;

  public MainRepo(WifiData wifiData) {
    this.wifiData = wifiData;
  }

  public Observable<String> observableWifiGateway() {
    return wifiData
        .observableWifi()
        .flatMap(
            isWifi -> {
              if (isWifi) return wifiData.observableWifiGateway();
              else return Observable.empty();
            });
  }

  public void registerNetworkCallback() {
    wifiData.registerNetworkCallback();
  }

  public void unregisterNetworkCallback() {
    wifiData.unregisterNetworkCallback();
  }
}
