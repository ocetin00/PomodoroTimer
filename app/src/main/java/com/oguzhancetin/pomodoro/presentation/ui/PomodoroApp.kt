package com.oguzhancetin.pomodoro.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroApp() {
    PomodoroTheme {
        val navController = rememberNavController()
        PomodoroNavGraph(
            navController = navController
        )
    }

}



