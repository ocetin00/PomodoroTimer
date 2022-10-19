package com.oguzhancetin.pomodoro.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oguzhancetin.pomodoro.ui.theme.PomodoroTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroApp() {
    PomodoroTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            PomodoroNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: PomodoroDestinations.MAIN_ROUTE

        val drawerState = rememberDrawerState(DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    navigateToMain = navigationActions.navigateToMain,
                    navigateToSetting = navigationActions.navigateToSetting,
                    navigateToStatus = navigationActions.navigateToSetting,
                    navigateToTask = navigationActions.navigateToTask,
                    closeDrawer = { coroutineScope.launch { drawerState.close() } })
            }) {
            Row {
                PomodoroNavGraph(
                    navController = navController,
                    openDrawer = { coroutineScope.launch { drawerState.open() } }
                  )
            }
        }
    }

}
