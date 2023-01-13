package com.oguzhancetin.pomodoro.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oguzhancetin.pomodoro.screen.main.MainScreen
import com.oguzhancetin.pomodoro.screen.task.TaskScreen


@Composable
fun PomodoroNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = PomodoroDestinations.MAIN_ROUTE
) {

    val onBack: () -> Unit = PomodoroNavigationActions(navController).navigateToMain

    NavHost(navController = navController, startDestination = startDestination) {
        composable(PomodoroDestinations.MAIN_ROUTE) {
            MainScreen(
                modifier = modifier,
                navController = navController,
                onAddTaskButtonClicked = { navController.navigate(PomodoroDestinations.TASK_ROUTE) })
        }
        composable(PomodoroDestinations.SETTING_ROUTE) {
            SettingScreen(modifier = modifier, onBack = { onBack.invoke() })
        }
        composable(PomodoroDestinations.TASK_ROUTE) {
            TaskScreen(modifier = modifier, onBack = { onBack.invoke() })
        }
        composable(PomodoroDestinations.STATUS_ROUTE) {
            StatusScreen(modifier = modifier, onBack = { onBack.invoke() })
        }
    }

}
