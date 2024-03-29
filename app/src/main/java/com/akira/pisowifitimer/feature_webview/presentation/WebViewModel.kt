package com.akira.pisowifitimer.feature_webview.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akira.pisowifitimer.core.util.Resource
import com.akira.pisowifitimer.feature_webview.domain.repository.WebRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WebRepository) : ViewModel() {
    private val _wifi = mutableStateOf(WebState())
    val wifi: State<WebState> = _wifi

    private val _uiState = MutableSharedFlow<UIState>()
    val uiState = _uiState.asSharedFlow()

    init {
        getWifiGateWay()
    }

    private fun getWifiGateWay() {
        viewModelScope.launch {
            repository.onStart()
            repository.getWifiGateWay().collect { result ->
                when (result) {
                    is Resource.Success -> _wifi.value = wifi.value.copy(
                        gateway = result.data ?: "",
                        isConnected = true
                    )

                    is Resource.Error -> {
                        _wifi.value = wifi.value.copy(
                            isConnected = false
                        )
                        _uiState.emit(
                            UIState.ShowSnackbar(
                                message = "Wi-Fi not connected"
                            )
                        )

                    }
                }
            }
        }.invokeOnCompletion {
            repository.onStop()
        }
    }
}


sealed class UIState {
    data class ShowSnackbar(val message: String) : UIState()
}