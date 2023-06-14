package com.oguzhancetin.pomodoro.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorRes
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.common.util.preference.IS_DARK_MODE_KEY
import com.oguzhancetin.pomodoro.common.util.preference.dataStore
import com.oguzhancetin.pomodoro.data.local.PomodoroDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appDb: PomodoroDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val enableDarkMode = this.dataStore.data
            .map { preferences ->
                preferences[IS_DARK_MODE_KEY] ?: false
            }

        setContent {
            val isDarkMode by enableDarkMode.collectAsState(initial = false)
            Log.d("ThemeState", "theme: $isDarkMode")
            LaunchedEffect(isDarkMode) {
                @ColorRes val backGroundColor =
                    if (isDarkMode) R.color.colorPrimaryDark else R.color.colorPrimary
                window.decorView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        backGroundColor
                    )
                )
            }
            PomodoroApp(isDarkMode)
        }

    }
}


