package com.akira.pisowifitimer.feature_history.domain.use_case

import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository

class GetEndDate(private val historyRepository: HistoryRepository) {
    suspend operator fun invoke(): String = historyRepository.getEndDate()
}