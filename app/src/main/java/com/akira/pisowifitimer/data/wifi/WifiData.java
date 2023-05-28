package com.akira.pisowifitimer.data.wifi;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.RouteInfo;
import android.os.Build;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import java.net.InetAddress;
import android.net.Network;
import dagger.hilt.android.qualifiers.ActivityContext;
import javax.inject.Inject;

public class WifiData {
  private ConnectivityManager connectivityManager;
  private final PublishSubject<String> gatewaySubject = PublishSubject.create();
  private final PublishSubject<Boolean> wifiSubject = PublishSubject.create();
  private final ConnectivityManager.NetworkCallback callback =
      new ConnectivityManager.NetworkCallback() {

        @TargetApi(29)
        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties properties) {
          super.onLinkPropertiesChanged(network, properties);
          for (RouteInfo route : properties.getRoutes()) {
            if (route.hasGateway()) {
              InetAddress gateway = route.getGateway();
              gatewaySubject.onNext("https://" + gateway.getHostAddress());
              break;
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
              InetAddress gateway = route.getGateway();
              gatewaySubject.onNext("https://" + gateway.getHostAddress());
              break;
            }
          }
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities capabilities) {
          super.onCapabilitiesChanged(network, capabilities);
          if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            wifiSubject.onNext(true);
          else wifiSubject.onNext(false);
        }
      };

  @Inject
  public WifiData(@ActivityContext Context context) {
    this.connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  public Observable<String> observableWifiGateway() {
    return gatewaySubject;
  }

  public Observable<Boolean> observableWifi() {
    return wifiSubject;
  }

  public void registerNetworkCallback() {
    NetworkRequest request =
        new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build();

    connectivityManager.registerNetworkCallback(request, callback);
  }

  public void unregisterNetworkCallback() {
    connectivityManager.unregisterNetworkCallback(callback);
  }
}
