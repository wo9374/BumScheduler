package com.ljb.bumscheduler.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class Reducer<S : UiState, E : UiEvent>(initialState: S) {
    private val _uiState = MutableStateFlow(initialState)
    val uiState get() = _uiState.asStateFlow()

    fun sendEvent(event: E) {
        _uiState.update { currentState ->
            reduce(currentState, event)
        }
    }

    abstract fun reduce(oldState: S, event: E): S
}

interface UiState

interface UiEvent