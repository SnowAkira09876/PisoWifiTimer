package com.akira.pisowifitimer.feature_webview.data.local

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.akira.pisowifitimer.core.util.Resource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WifiApi(
    private val connectivityManager: ConnectivityManager,
    private val networkRequest: NetworkRequest
) {
    private val networkCallback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onLinkPropertiesChanged(network: Network, properties: LinkProperties) {
                super.onLinkPropertiesChanged(network, properties)
                for (route in properties.routes) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q && route.hasGateway()) {
                        val gateway = route.gateway
                        _gateway.tryEmit(Resource.Success("https://" + gateway?.hostAddress))
                        break
                    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        val gateway = route.gateway
                        _gateway.tryEmit(Resource.Success("https://" + gateway?.hostAddress))
                        break
                    }
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, capabilities)
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    _wifi.tryEmit(Resource.Success(true))
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _wifi.tryEmit(Resource.Error())
            }
        }

    private val _gateway = MutableSharedFlow<Resource<String>>(
        replay = 0,
        extraBufferCapacity = 1,
        BufferOverflow.DROP_OLDEST
    )
    val gateway = _gateway.asSharedFlow()

    private val _wifi = MutableSharedFlow<Resource<Boolean>>(
        replay = 0,
        extraBufferCapacity = 1,
        BufferOverflow.DROP_OLDEST
    )
    val wifi = _wifi.asSharedFlow()

    fun startListening() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager.requestNetwork(networkRequest, networkCallback)
        }
    }

    fun stopListening() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}