package com.akira.pisowifitimer.core.data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import com.akira.pisowifitimer.feature_history.data.local.HistoryInfoDao
import com.akira.pisowifitimer.feature_timer.data.local.TimeInfoDao
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo

@Database(
    entities = [TimeInfo::class],
    version = 1
)
abstract class AppRoomDatabase: RoomDatabase() {

    abstract val timeInfoDao: TimeInfoDao

    abstract val historyInfoDao: HistoryInfoDao

    companion object {
        const val DATABASE_NAME = "room_db"
    }
}