package com.ljb.bumscheduler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.data.repository.DEVICE_MODE
import com.ljb.domain.usecase.GetDarkModeUseCase
import com.ljb.domain.usecase.SaveDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val saveDarkModeUseCase: SaveDarkModeUseCase,
): ViewModel() {
    private val _darkModeState = MutableStateFlow(DEVICE_MODE)
    val darkModeState get() = _darkModeState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getDarkModeUseCase().collectLatest { mode ->
                _darkModeState.update { mode }
            }
        }
    }

    fun setDarkModePrefs(mode: String) = viewModelScope.launch(Dispatchers.IO) {
        saveDarkModeUseCase(mode)
    }
}