package com.akira.pisowifitimer.feature_timer.di

import com.akira.pisowifitimer.core.data.local.AppRoomDatabase
import com.akira.pisowifitimer.feature_timer.data.repository.TimerRepositoryImpl
import com.akira.pisowifitimer.feature_timer.domain.repository.TimerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {
    @Provides
    @Singleton
    fun provideTimerRepository(db: AppRoomDatabase): TimerRepository {
        return TimerRepositoryImpl(db.timeInfoDao)
    }
}