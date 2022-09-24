package com.oguzhancetin.pomodoro.screen.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.oguzhancetin.pomodoro.ui.AppBar
import com.oguzhancetin.pomodoro.ui.PomodoroNavGraph
import com.oguzhancetin.pomodoro.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Scaffold(
    navController: NavHostController,
    topBarIcon: ImageVector,
    navigationIconClick: ()->Unit
){
    androidx.compose.material3.Scaffold(
        topBar = {
            AppBar(onNavigationIconClick = navigationIconClick)
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
        },
/*floatingActionButton = {
    FloatingActionButton(onClick = {  }) {
        Icon(imageVector = Icons.Filled.Create,"create_task")
    }
},*/
        floatingActionButtonPosition = FabPosition.End,
//  containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
    ) { padding ->
        PomodoroNavGraph(
            modifier = androidx.compose.ui.Modifier
                .padding(padding)
                .fillMaxSize(), navController = navController
        )

    }
}
