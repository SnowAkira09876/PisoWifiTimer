package com.akira.pisowifitimer.feature_timer.data.local

import androidx.room.Dao
import androidx.room.Insert
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo


@Dao
interface TimeInfoDao {
    @Insert
    suspend fun insert(model: TimeInfo)
}