package com.akira.pisowifitimer.feature_history.data.local

import androidx.room.Dao
import androidx.room.Query
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryInfoDao {
    @Query("SELECT * FROM timeinfo ORDER BY id DESC")
    fun getHistory(): Flow<List<TimeInfo>>

    @Query("SELECT SUM(amount) FROM timeinfo")
    suspend fun getTotalAmount(): Int

    @Query("SELECT SUM(amount) FROM timeinfo WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalAmountBetween(startDate: String, endDate: String): Int

    @Query("SELECT * FROM timeinfo WHERE date BETWEEN :startDate AND :endDate")
    fun getHistoryBetween(startDate: String, endDate: String): Flow<List<TimeInfo>>

    @Query("SELECT date FROM timeinfo ORDER BY id ASC LIMIT 1")
    suspend fun getStartDate(): String

    @Query("SELECT date FROM timeinfo ORDER BY id DESC LIMIT 1")
    suspend fun getEndDate(): String

    @Query("SELECT COUNT(*) FROM timeinfo")
    suspend fun getTotalTime(): Int
}