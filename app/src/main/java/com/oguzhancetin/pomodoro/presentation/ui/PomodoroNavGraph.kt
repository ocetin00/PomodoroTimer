package com.oguzhancetin.pomodoro.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.oguzhancetin.pomodoro.presentation.screen.main.MainScreen
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskScreen
import com.oguzhancetin.pomodoro.ui.SettingScreen
import com.oguzhancetin.pomodoro.presentation.screen.report.StatusScreen
import com.oguzhancetin.pomodoro.presentation.screen.splash.SplashScreen
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskDetailScreen


@Composable
fun PomodoroNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = PomodoroDestinations.SPLASH_ROUTE
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
        composable(PomodoroDestinations.TASK_ROUTE) { navBackStackEntry ->
            TaskScreen(
                modifier = modifier,
                onBack = { onBack.invoke() },
                onNavigateTaskDetail = {
                    navController.navigate("${PomodoroDestinations.SPLASH_ROUTE}/{${it.toString()}}")
                })
        }
        composable(PomodoroDestinations.STATUS_ROUTE) {
            StatusScreen(modifier = modifier, onBack = { onBack.invoke() })
        }
        composable(PomodoroDestinations.SPLASH_ROUTE) {
            SplashScreen(
                modifier = modifier,
                onAnimationDone = { navController.navigate(PomodoroDestinations.MAIN_ROUTE) })
        }
        composable(
            "${PomodoroDestinations.SPLASH_ROUTE}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            TaskDetailScreen(modifier = modifier,
                onBack = { onBack.invoke() }
            )
        }
    }

}
