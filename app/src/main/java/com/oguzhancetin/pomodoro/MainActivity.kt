package com.oguzhancetin.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oguzhancetin.pomodoro.ui.AppBar
import com.oguzhancetin.pomodoro.ui.MyAppNavHost
import com.oguzhancetin.pomodoro.ui.NavigationDrawer
import com.oguzhancetin.pomodoro.ui.Screen
import com.oguzhancetin.pomodoro.ui.theme.PomodoroTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            PomodoroTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()

                ) {
                    PomodoroApp()

                }

            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroApp() {
    val screens = listOf(
        DrawerScreens.Home,
        DrawerScreens.Account,
        DrawerScreens.Help
    )

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = screens
    val selectedItem = remember { mutableStateOf(items[0]) }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = {  },
                        label = { Text(item.title) },
                        selected = item == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                            navController.navigate(item.route)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Scaffold(
                    topBar = {
                        AppBar(onNavigationIconClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        })
                    },
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {  }) {
                            Icon(imageVector = Icons.Filled.Create,"create_task")
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    //  containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                ) { padding ->
                    MyAppNavHost(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(), navController = navController, Screen.Main.route
                    )

                }

            }
        })

}

sealed class DrawerScreens(val title: String,val route:String) {
    object Home : DrawerScreens("main","main")
    object Account : DrawerScreens("report","report")
    object Help : DrawerScreens( "setting","setting")
}

