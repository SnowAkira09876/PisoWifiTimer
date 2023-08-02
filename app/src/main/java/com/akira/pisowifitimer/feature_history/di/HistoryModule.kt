package com.akira.pisowifitimer.feature_history.di

import com.akira.pisowifitimer.core.data.local.AppRoomDatabase
import com.akira.pisowifitimer.feature_history.data.repository.HistoryRepositoryImpl
import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository
import com.akira.pisowifitimer.feature_history.domain.use_case.GetHistory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {
    @Provides
    @Singleton
    fun provideHistoryRepository(db: AppRoomDatabase): HistoryRepository =
        HistoryRepositoryImpl(db.historyInfoDao)

    @Provides
    @Singleton
    fun provideGetHistory(historyRepository: HistoryRepository): GetHistory =
        GetHistory(historyRepository)

}