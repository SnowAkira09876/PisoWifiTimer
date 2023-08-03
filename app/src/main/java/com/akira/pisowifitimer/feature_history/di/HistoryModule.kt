package com.akira.pisowifitimer.feature_history.di

import com.akira.pisowifitimer.core.data.local.AppRoomDatabase
import com.akira.pisowifitimer.feature_history.data.repository.HistoryRepositoryImpl
import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository
import com.akira.pisowifitimer.feature_history.domain.use_case.GetEndDate
import com.akira.pisowifitimer.feature_history.domain.use_case.GetHistory
import com.akira.pisowifitimer.feature_history.domain.use_case.GetHistoryBetween
import com.akira.pisowifitimer.feature_history.domain.use_case.GetStartDate
import com.akira.pisowifitimer.feature_history.domain.use_case.GetTotalAmount
import com.akira.pisowifitimer.feature_history.domain.use_case.GetTotalAmountBetween
import com.akira.pisowifitimer.feature_history.domain.use_case.GetTotalTime
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
    fun provideGetHistoryUseCase(historyRepository: HistoryRepository): GetHistory =
        GetHistory(historyRepository)

    @Provides
    @Singleton
    fun provideGetHistoryBetweenUseCase(historyRepository: HistoryRepository): GetHistoryBetween =
        GetHistoryBetween(historyRepository)

    @Provides
    @Singleton
    fun provideGetTotalAmountUseCase(historyRepository: HistoryRepository): GetTotalAmount =
        GetTotalAmount(historyRepository)

    @Provides
    @Singleton
    fun provideGetTotalAmountBetweenUseCase(historyRepository: HistoryRepository): GetTotalAmountBetween =
        GetTotalAmountBetween(historyRepository)

    @Provides
    @Singleton
    fun provideGetTotalTimeUseCase(historyRepository: HistoryRepository): GetTotalTime =
        GetTotalTime(historyRepository)

    @Provides
    @Singleton
    fun provideGetStartDateUseCase(historyRepository: HistoryRepository): GetStartDate =
        GetStartDate(historyRepository)

    @Provides
    @Singleton
    fun provideGetEndDateUseCase(historyRepository: HistoryRepository): GetEndDate =
        GetEndDate(historyRepository)

}