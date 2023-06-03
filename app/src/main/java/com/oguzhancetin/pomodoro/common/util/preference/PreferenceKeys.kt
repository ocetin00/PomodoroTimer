package com.oguzhancetin.pomodoro.common.util.preference

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey



val IS_DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

val IS_SILENT_NOTIFICATION = booleanPreferencesKey("silent_notification")