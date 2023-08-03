package com.akira.pisowifitimer.feature_history.domain.use_case

import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository

class GetTotalTime(private val historyRepository: HistoryRepository) {
    suspend operator fun invoke(): Int = historyRepository.getTotalTime()
}