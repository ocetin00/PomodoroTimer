package com.oguzhancetin.pomodoro.presentation.screen.setting

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.common.util.preference.IS_DARK_MODE_KEY
import com.oguzhancetin.pomodoro.common.util.preference.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MAX_MINUTE = 99
const val MIN_MINUTE = 0

@HiltViewModel
class SettingViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {
    var longTime:Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Long().getPrefKey()] ?: 0
            
        }
    var shortTime:Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Short().getPrefKey()] ?: 0
        }
    var pomodoroTime:Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Pomodoro().getPrefKey()] ?: 0
        }

    var isDarkTheme:Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: false
        }


    fun increaseTime(time:Times) {
        viewModelScope.launch (Dispatchers.IO){
            context.dataStore.edit { settings->
                val currentValue = settings[time.getPrefKey()] ?: 0
                if(currentValue.div(60000) <= MAX_MINUTE){
                    settings[time.getPrefKey()] = currentValue + 60000
                }

            }
        }
    }
    fun decreaseTime(time:Times) {
        viewModelScope.launch (Dispatchers.IO){
            context.dataStore.edit { settings->
                val currentValue = settings[time.getPrefKey()] ?: 0
                if(currentValue > MIN_MINUTE){
                    settings[time.getPrefKey()] = currentValue - 60000
                }
            }
        }
    }

    fun ToggleAppTheme(){
        viewModelScope.launch (Dispatchers.IO){
            context.dataStore.edit { settings->
                val isDarkTheme = settings[IS_DARK_MODE_KEY] ?: false
                settings[IS_DARK_MODE_KEY] = !isDarkTheme
            }
        }
    }


}