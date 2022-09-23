package com.oguzhancetin.pomodoro.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oguzhancetin.pomodoro.screen.main.MainScreen


@Composable
fun MyAppNavHost(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String,

){
    NavHost(navController = navController, startDestination = startDestination){
        composable("main"){
            MainScreen(modifier = modifier)
        }
        composable("report"){
            ReportScreen(modifier)
        }
        composable("setting"){
            SettingScreen(modifier)
        }
    }
}