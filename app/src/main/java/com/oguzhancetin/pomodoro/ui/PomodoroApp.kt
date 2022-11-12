package com.oguzhancetin.pomodoro.ui

import android.R
import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oguzhancetin.pomodoro.ui.commonUI.UpperFirstChar
import com.oguzhancetin.pomodoro.ui.theme.PomodoroTheme
import com.oguzhancetin.pomodoro.ui.theme.light_onRedBackground
import kotlinx.coroutines.delay
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
                    navigateToStatus = navigationActions.navigateToStatus,
                    navigateToTask = navigationActions.navigateToTask,
                    closeDrawer = { coroutineScope.launch { delay(250); drawerState.close() } })
            }) {

            Scaffold(

                topBar = {
                    MainAppBar(
                        currentRoute = currentRoute.UpperFirstChar(),
                        openDrawer = {coroutineScope.launch { drawerState.open()}},
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp()} )
                },
                floatingActionButtonPosition = FabPosition.End,
              /*  floatingActionButton = {
                    if (favoriteTaskItems.isNotEmpty()){

                        ExtendedFloatingActionButton(
                            onClick = {
                                openTaskScreen()
                            }) {
                            Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Add")
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Add Task")
                            }

                        }
                    }

                }*/
            ) { innerPadding ->
                Row {
                    PomodoroNavGraph(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController
                    )
                }
            }
            }


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    canNavigateBack: Boolean,
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState),
    navigateUp: () -> Unit,
    currentRoute: String
) {
    val title = currentRoute
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = { Text(text = title, color = light_onRedBackground) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }else{
                IconButton(onClick = openDrawer) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        }/*,
        actions = {
            IconButton(onClick = { *//* TODO: Open search *//* }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.menu),
                    tint = light_onSurfaceRed
                )
            }
        }*/,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}
