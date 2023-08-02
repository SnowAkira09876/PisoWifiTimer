package com.akira.pisowifitimer.feature_timer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimeInfo(
    val time: String?,
    val date: String?,
    val amount: String?,
    @PrimaryKey val id: Int? = null
)