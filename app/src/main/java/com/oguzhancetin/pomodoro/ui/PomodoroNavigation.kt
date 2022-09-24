package com.oguzhancetin.pomodoro.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController


object PomodoroDestinations{
    const val MAIN_ROUTE = "main"
    const val SETTING_ROUTE = "setting"
    const val STATUS_ROUTE = "status"
}

class PomodoroNavigationActions(navController: NavHostController){
    val navigateToMain: ()-> Unit = {
        navController.navigate(PomodoroDestinations.MAIN_ROUTE){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            /**
             * avoid multiple same destination when select again same item
             */
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
}

