package com.akira.pisowifitimer.di

import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.room.Room
import androidx.work.WorkManager
import com.akira.pisowifitimer.core.data.local.AppRoomDatabase
import com.akira.pisowifitimer.feature_webview.util.AppChromeClient
import com.akira.pisowifitimer.feature_webview.util.AppWebClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Singleton
    @Provides
    fun provideNetworkRequest(): NetworkRequest = NetworkRequest.Builder().addTransportType(
        NetworkCapabilities.TRANSPORT_WIFI
    ).build()

    @Singleton
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Singleton
    @Provides
    fun provideWebChromeClient(): AppChromeClient = AppChromeClient()

    @Singleton
    @Provides
    fun provideWebClient(): AppWebClient = AppWebClient()

    @Provides
    @Singleton
    fun provideAppRoomDatabase(@ApplicationContext context: Context): AppRoomDatabase {
        return Room.databaseBuilder(
            context,
            AppRoomDatabase::class.java,
            AppRoomDatabase.DATABASE_NAME
        ).build()
    }
}