package com.oguzhancetin.pomodoro.ui

import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oguzhancetin.pomodoro.screen.main.MainScreen


@Composable
fun PomodoroNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    openDrawer: () -> Unit = {},
    navigateFirstScreen: ()-> Unit,
    startDestination: String = PomodoroDestinations.MAIN_ROUTE,

    ) {
    Surface(
        color = MaterialTheme.colorScheme.primary
    ) {

        NavHost(navController = navController, startDestination = startDestination) {
            composable(PomodoroDestinations.MAIN_ROUTE) {
                MainScreen(modifier = modifier, openDrawer = openDrawer)
            }
            composable(PomodoroDestinations.STATUS_ROUTE) {
                ReportScreen(modifier = modifier, openDrawer = openDrawer)
            }
            composable(PomodoroDestinations.SETTING_ROUTE) {
                SettingScreen(modifier = modifier, goBack = navigateFirstScreen)
            }
        }
    }
}
