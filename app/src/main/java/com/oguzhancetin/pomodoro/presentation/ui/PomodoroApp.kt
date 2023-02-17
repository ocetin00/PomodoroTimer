package com.oguzhancetin.pomodoro.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.UpperFirstChar
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme
import com.oguzhancetin.pomodoro.presentation.ui.theme.light_onRedBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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



