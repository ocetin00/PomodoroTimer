package com.oguzhancetin.pomodoro.presentation.screen.setting

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.core.time.Time
import com.oguzhancetin.pomodoro.core.preference.IS_DARK_MODE_KEY
import com.oguzhancetin.pomodoro.core.preference.IS_SILENT_NOTIFICATION
import com.oguzhancetin.pomodoro.core.preference.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MAX_MINUTE = 99
const val MIN_MINUTE = 0


class SettingViewModel @Inject constructor( val context: Context) : ViewModel() {
    var longTime:Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Time.Long().getPrefKey()] ?: 0
        }
    var shortTime:Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Time.Short().getPrefKey()] ?: 0
        }
    var pomodoroTime:Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Time.Pomodoro().getPrefKey()] ?: 0
        }

    var isDarkTheme:Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: false
        }
     var isSilentNotification: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_SILENT_NOTIFICATION] ?: false
        }


    fun increaseTime(time: Time) {
        viewModelScope.launch (Dispatchers.IO){
            context.dataStore.edit { settings->
                val currentValue = settings[time.getPrefKey()] ?: 0
                if(currentValue.div(60000) <= MAX_MINUTE){
                    settings[time.getPrefKey()] = currentValue + 60000
                }

            }
        }
    }
    fun decreaseTime(time: Time) {
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

    fun toggleTickSound() {
        viewModelScope.launch (Dispatchers.IO){
            context.dataStore.edit { settings->
                val isSilentNotification = settings[IS_SILENT_NOTIFICATION] ?: false
                settings[IS_SILENT_NOTIFICATION] = !isSilentNotification
            }
        }
    }


}