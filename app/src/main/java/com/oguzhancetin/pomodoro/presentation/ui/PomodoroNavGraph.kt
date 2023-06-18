package com.oguzhancetin.pomodoro.presentation.ui


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController

import androidx.navigation.NavType

import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost

import com.oguzhancetin.pomodoro.presentation.screen.main.MainScreen
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskScreen
import com.oguzhancetin.pomodoro.ui.SettingScreen
import com.oguzhancetin.pomodoro.presentation.screen.report.StatusScreen
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskDetailScreen


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PomodoroNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = PomodoroDestinations.MAIN_ROUTE,
) {

    val onBack: () -> Unit = PomodoroNavigationActions(navController).navigateToMain
    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        composable(
            PomodoroDestinations.MAIN_ROUTE
        ) {
            MainScreen(
                modifier = modifier,
                navController = navController,
                onAddTaskButtonClicked = { navController.navigate(PomodoroDestinations.TASK_ROUTE) })
        }
        composable(
            PomodoroDestinations.SETTING_ROUTE,
            enterTransition = {
                when (initialState.destination.route) {
                    PomodoroDestinations.MAIN_ROUTE ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    PomodoroDestinations.MAIN_ROUTE ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )

                    else -> null
                }
            },
        ) {
            SettingScreen(modifier = modifier, onBack = { onBack.invoke() })
        }
        composable(
            PomodoroDestinations.TASK_ROUTE,
            enterTransition = {
                when (initialState.destination.route) {
                    PomodoroDestinations.MAIN_ROUTE ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    PomodoroDestinations.MAIN_ROUTE ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                    else -> null
                }
            },
        ) { navBackStackEntry ->
            TaskScreen(
                modifier = modifier,
                onBack = { onBack.invoke() },
                onNavigateTaskDetail = { taskId, categoryId ->

                    navController.navigate(
                        "${PomodoroDestinations.TASK_DETAIL_ROUTE}/{${taskId.toString()}}/{$categoryId}"
                    )


                })
        }
        composable(
            PomodoroDestinations.STATUS_ROUTE,
            enterTransition = {
                when (initialState.destination.route) {
                    PomodoroDestinations.MAIN_ROUTE ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    PomodoroDestinations.MAIN_ROUTE ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                    else -> null
                }
            },
        ) {
            StatusScreen(modifier = modifier, onBack = { onBack.invoke() })
        }
        composable(
            route = "${PomodoroDestinations.TASK_DETAIL_ROUTE}/{taskId}/{selectedCategoryId}",
            arguments = listOf(
                navArgument("taskId") { type = NavType.StringType },
                navArgument("selectedCategoryId") { type = NavType.StringType }),
            enterTransition = {
                when (initialState.destination.route) {
                    PomodoroDestinations.TASK_ROUTE ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    PomodoroDestinations.TASK_ROUTE ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                    else -> null
                }
            },

            ) {
            TaskDetailScreen(modifier = modifier,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
