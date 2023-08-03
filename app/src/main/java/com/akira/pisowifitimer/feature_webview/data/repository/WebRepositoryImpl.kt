package com.akira.pisowifitimer.feature_webview.data.repository

import com.akira.pisowifitimer.core.util.Resource
import com.akira.pisowifitimer.feature_webview.data.local.WifiApi
import com.akira.pisowifitimer.feature_webview.domain.repository.WebRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class WebRepositoryImpl(
    private val wifiApi: WifiApi
) :
    WebRepository {
    override fun getWifiGateWay(): Flow<Resource<String>> =
        combine(wifiApi.wifi, wifiApi.gateway) { wifi, gateway ->
            when (wifi) {
                is Resource.Success -> gateway
                else -> Resource.Error()
            }
        }

    override fun onStart() {
        wifiApi.startListening()
    }

    override fun onStop() {
        wifiApi.stopListening()
    }
}