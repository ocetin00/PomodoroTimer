package com.oguzhancetin.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oguzhancetin.pomodoro.ui.MyAppNavHost
import com.oguzhancetin.pomodoro.ui.Screen
import com.oguzhancetin.pomodoro.ui.theme.PomodoroTheme
import dagger.hilt.android.AndroidEntryPoint

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
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Pomodoro", color = MaterialTheme.colorScheme.onPrimary) },
                colors =
                TopAppBarDefaults
                    .centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "menu",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
        }

        //  containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
    ) {
        MyAppNavHost(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), navController = navController, Screen.Main.route
        )

    }
}

