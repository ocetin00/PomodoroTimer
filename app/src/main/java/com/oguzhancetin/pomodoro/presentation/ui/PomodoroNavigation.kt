package com.oguzhancetin.pomodoro.presentation.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController


object PomodoroDestinations{
    const val MAIN_ROUTE = "pomodoro"
    const val SETTING_ROUTE = "setting"
    const val STATUS_ROUTE = "status"
    const val TASK_ROUTE = "task"
    const val SPLASH_ROUTE = "splash"
}

class PomodoroNavigationActions(navController: NavHostController){


    val navigateToMain: ()-> Unit = {
        navController.navigate(PomodoroDestinations.MAIN_ROUTE){
            navController.currentDestination?.route?.let {
                popUpTo(it){
                    saveState = true
                    inclusive = true
                }
            }

            launchSingleTop = true
            restoreState = true
        }


    }
    val navigateToSetting: ()-> Unit = {
        navController.navigate(PomodoroDestinations.SETTING_ROUTE){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }

    }
    val navigateToStatus: ()-> Unit = {
        navController.navigate(PomodoroDestinations.STATUS_ROUTE){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }

    }
    val navigateToTask: ()-> Unit = {
        navController.navigate(PomodoroDestinations.TASK_ROUTE){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }

    }
}

