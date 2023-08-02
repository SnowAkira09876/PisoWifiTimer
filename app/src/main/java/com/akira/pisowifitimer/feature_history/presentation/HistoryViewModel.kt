package com.akira.pisowifitimer.feature_history.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akira.pisowifitimer.feature_history.domain.use_case.GetHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val getHistory: GetHistory) : ViewModel() {
    private val _state = mutableStateOf(HistoryState())
    val state: State<HistoryState> = _state

    init {
        getHistoryList()
    }

    private fun getHistoryList() {
        viewModelScope.launch {
            getHistory().collect { history ->
                _state.value = state.value.copy(
                    history = history,
                )
            }
        }
    }
}