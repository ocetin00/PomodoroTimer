package com.oguzhancetin.pomodoro.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PomodoroApp() {
    val navController = rememberAnimatedNavController()
    PomodoroNavGraph(
        navController = navController
    )
}



