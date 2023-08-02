package com.akira.pisowifitimer.feature_home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akira.pisowifitimer.core.util.Resource
import com.akira.pisowifitimer.feature_home.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {
    private val _wifi = mutableStateOf(HomeState())
    val wifi: State<HomeState> = _wifi

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
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


sealed class UIEvent {
    data class ShowSnackbar(val message: String) : UIEvent()
}