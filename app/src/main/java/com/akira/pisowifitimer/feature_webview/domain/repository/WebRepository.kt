package com.akira.pisowifitimer.feature_webview.domain.repository

import com.akira.pisowifitimer.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface WebRepository {
    fun getWifiGateWay(): Flow<Resource<String>>

    fun onStart()
    fun onStop()
}