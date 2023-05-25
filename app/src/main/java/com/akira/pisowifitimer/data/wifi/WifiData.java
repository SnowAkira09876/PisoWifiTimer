package com.akira.pisowifitimer.data.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.RouteInfo;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import java.net.InetAddress;
import android.net.Network;
import dagger.hilt.android.qualifiers.ActivityContext;
import javax.inject.Inject;

public class WifiData {
  private ConnectivityManager connectivityManager;
  private final PublishSubject<String> networkSubject = PublishSubject.create();
  private final ConnectivityManager.NetworkCallback callback =
      new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
          super.onAvailable(network);
          LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
          for (RouteInfo route : linkProperties.getRoutes()) {
            if (route.hasGateway()) {
              InetAddress gateway = route.getGateway();
              networkSubject.onNext("https://" + gateway.getHostAddress());
              break;
            }
          }
        }
      };

  @Inject
  public WifiData(@ActivityContext Context context) {
    this.connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  public Observable<String> observableWifiGateway() {
    return networkSubject;
  }

  public void registerNetworkCallback() {
    connectivityManager.registerDefaultNetworkCallback(callback);
  }

  public void unregisterNetworkCallback() {
    connectivityManager.unregisterNetworkCallback(callback);
  }
}
