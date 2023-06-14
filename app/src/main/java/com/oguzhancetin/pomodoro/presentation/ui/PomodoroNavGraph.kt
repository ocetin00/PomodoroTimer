package com.oguzhancetin.pomodoro.presentation.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.navigation
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.oguzhancetin.pomodoro.presentation.screen.TestScreen
import com.oguzhancetin.pomodoro.presentation.screen.main.MainScreen
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskScreen
import com.oguzhancetin.pomodoro.ui.SettingScreen
import com.oguzhancetin.pomodoro.presentation.screen.report.StatusScreen
import com.oguzhancetin.pomodoro.presentation.screen.splash.SplashScreen
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskDetailScreen


@ExperimentalAnimationApi
@Composable
fun PomodoroNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = PomodoroDestinations.MAIN_ROUTE,
) {
    val _navController = rememberAnimatedNavController()
    val onBack: () -> Unit = PomodoroNavigationActions(navController).navigateToMain

    AnimatedNavHost(navController = _navController, startDestination = "test") {
        composable(
            "test",
        ) {
            TestScreen()
        }

    }
}

/* composable(PomodoroDestinations.SETTING_ROUTE) {
             SettingScreen(modifier = modifier, onBack = { onBack.invoke() })
         }
         composable(PomodoroDestinations.TASK_ROUTE) { navBackStackEntry ->
             TaskScreen(
                 modifier = modifier,
                 onBack = { onBack.invoke() },
                 onNavigateTaskDetail = { taskId, categoryId ->

                     navController.navigate(
                         "${PomodoroDestinations.TASK_DETAIL_ROUTE}/{${taskId.toString()}}/{$categoryId}"
                     )


                 })
         }
         composable(PomodoroDestinations.STATUS_ROUTE) {
             StatusScreen(modifier = modifier, onBack = { onBack.invoke() })
         }
         composable(
             "${PomodoroDestinations.TASK_DETAIL_ROUTE}/{taskId}/{selectedCategoryId}",
             arguments = listOf(
                 navArgument("taskId") { type = NavType.StringType },
                 navArgument("selectedCategoryId") { type = NavType.StringType })
         ) {
             TaskDetailScreen(modifier = modifier,
                 onBack = { navController.popBackStack() }
             )
         }*/