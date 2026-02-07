package com.example.credithelper.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base MVI ViewModel.
 * Intent -> reduce -> State
 * One-shot effects via Effect
 */
abstract class MviViewModel<Intent : Any, State : Any, Effect : Any> : ViewModel() {

    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>(extraBufferCapacity = 1)
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    protected abstract fun initialState(): State
    protected abstract suspend fun handleIntent(intent: Intent)

    protected fun reduce(block: (State) -> State) {
        _state.update(block)
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    fun dispatch(intent: Intent) {
        viewModelScope.launch {
            handleIntent(intent)
        }
    }
}
