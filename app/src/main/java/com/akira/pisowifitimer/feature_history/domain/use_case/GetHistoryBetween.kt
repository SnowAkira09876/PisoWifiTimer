package com.akira.pisowifitimer.feature_history.domain.use_case

import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHistoryBetween(private val historyRepository: HistoryRepository) {
    operator fun invoke(startDate: String, endDate: String): Flow<List<TimeInfo>> {
        if (startDate.isBlank() || endDate.isBlank()) {
            return flow { }
        }

        return historyRepository.getHistoryBetween(startDate, endDate)
    }
}