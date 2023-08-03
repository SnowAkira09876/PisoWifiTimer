package com.akira.pisowifitimer.feature_history.domain.use_case

import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository

class GetTotalAmountBetween(private val historyRepository: HistoryRepository) {
    suspend operator fun invoke(startDate: String, endDate: String): Int {
        if (startDate.isBlank() || endDate.isBlank()) {
            return 0
        }

        return historyRepository.getTotalAmountBetween(startDate, endDate)
    }
}