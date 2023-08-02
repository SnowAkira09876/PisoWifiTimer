package com.akira.pisowifitimer.feature_home.data.repository

import com.akira.pisowifitimer.core.util.Resource
import com.akira.pisowifitimer.feature_home.data.local.WifiApi
import com.akira.pisowifitimer.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class HomeRepositoryImpl(
    private val wifiApi: WifiApi
) :
    HomeRepository {
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