package com.oguzhancetin.pomodoro.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PomodoroApp() {
    val navController = rememberAnimatedNavController()
    PomodoroNavGraph(
        navController = navController
    )
}



