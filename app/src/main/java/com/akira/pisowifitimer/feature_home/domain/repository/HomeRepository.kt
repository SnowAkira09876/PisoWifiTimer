package com.akira.pisowifitimer.feature_home.domain.repository

import com.akira.pisowifitimer.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getWifiGateWay(): Flow<Resource<String>>

    fun onStart()
    fun onStop()
}