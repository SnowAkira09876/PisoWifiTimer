package com.akira.pisowifitimer.feature_history.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akira.pisowifitimer.feature_history.domain.use_case.GetEndDate
import com.akira.pisowifitimer.feature_history.domain.use_case.GetHistory
import com.akira.pisowifitimer.feature_history.domain.use_case.GetStartDate
import com.akira.pisowifitimer.feature_history.domain.use_case.GetTotalAmount
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistory,
    private val getTotalAmountUseCase: GetTotalAmount,
    private val getStartDateUseCase: GetStartDate,
    private val getEndDateUseCase: GetEndDate
) : ViewModel() {
    private val _timeInfos = mutableStateOf(listOf<TimeInfo>())
    val timeInfos: State<List<TimeInfo>> = _timeInfos

    private val _totalAmount = mutableStateOf(0)
    val totalAmount: State<Int> = _totalAmount

    private val _startDate = mutableStateOf("")
    val startDate: State<String> = _startDate

    private val _endDate = mutableStateOf("")
    val endDate: State<String> = _endDate

    init {
        getHistory()
        onEvent(HistoryEvent.TotalAmount)
        onEvent(HistoryEvent.StartDate)
        onEvent(HistoryEvent.EndDate)
    }

    private fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.TotalAmount -> viewModelScope.launch {
                _totalAmount.value = getTotalAmountUseCase()
            }

            HistoryEvent.EndDate -> viewModelScope.launch {
                _startDate.value = getStartDateUseCase()
            }

            HistoryEvent.StartDate -> viewModelScope.launch {
                _endDate.value = getEndDateUseCase()
            }
        }
    }

    private fun getHistory() {
        viewModelScope.launch {
            getHistoryUseCase().collect { history ->
                _timeInfos.value = history
            }
        }
    }
}