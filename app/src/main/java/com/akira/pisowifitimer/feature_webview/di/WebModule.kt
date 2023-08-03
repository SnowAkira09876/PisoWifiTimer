package com.akira.pisowifitimer.feature_webview.di

import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.akira.pisowifitimer.feature_webview.data.local.WifiApi
import com.akira.pisowifitimer.feature_webview.data.repository.WebRepositoryImpl
import com.akira.pisowifitimer.feature_webview.domain.repository.WebRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WebModule {
    @Provides
    fun provideWifiApi(
        connectivityManager: ConnectivityManager,
        networkRequest: NetworkRequest
    ): WifiApi = WifiApi(connectivityManager, networkRequest)

    @Provides
    fun provideHomeRepository(wifiApi: WifiApi): WebRepository = WebRepositoryImpl(wifiApi)
}