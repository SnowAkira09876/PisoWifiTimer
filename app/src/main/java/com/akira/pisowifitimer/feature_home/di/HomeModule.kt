package com.akira.pisowifitimer.feature_home.di

import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.akira.pisowifitimer.feature_home.data.local.WifiApi
import com.akira.pisowifitimer.feature_home.data.repository.HomeRepositoryImpl
import com.akira.pisowifitimer.feature_home.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    fun provideWifiApi(
        connectivityManager: ConnectivityManager,
        networkRequest: NetworkRequest
    ): WifiApi = WifiApi(connectivityManager, networkRequest)

    @Provides
    fun provideHomeRepository(wifiApi: WifiApi): HomeRepository = HomeRepositoryImpl(wifiApi)
}