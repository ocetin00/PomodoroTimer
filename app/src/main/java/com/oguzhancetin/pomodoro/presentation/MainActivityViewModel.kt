package com.oguzhancetin.pomodoro.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.MainActivityUiState
import com.oguzhancetin.pomodoro.core.preference.IS_DARK_MODE_KEY
import com.oguzhancetin.pomodoro.core.preference.dataStore
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivityViewModel  (
    val context: Application,
    val pomodoroRepository: PomodoroRepository
) : ViewModel() {


    private val _enableDarkMode = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: false
        }
    private val _isAnyPomodoro = MutableStateFlow(false)

    val uiState: StateFlow<MainActivityUiState> =
        combine(_isAnyPomodoro, _enableDarkMode) { isAnyPomodoro, enableDarkMode ->
            MainActivityUiState.Success(
                isDarkModel = enableDarkMode,
                isAnyPomodoro = isAnyPomodoro
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = MainActivityUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )


    init {
        viewModelScope.launch(Dispatchers.IO) {
            /**
             * if any pomodoro added to database then show in app review
             */
            val isAnyPomodoro = pomodoroRepository.isAnyPomodoroFinish()
            _isAnyPomodoro.value = isAnyPomodoro
        }
    }


}