package com.oguzhancetin.pomodoro.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.ui.StatelessTimer
import com.oguzhancetin.pomodoro.ui.theme.*
import com.oguzhancetin.pomodoro.util.Times

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("EnqueueWork")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    openDrawer: () -> Unit = {}
) {
    val currentTime = viewModel.currentTime

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            MainAppBar(openDrawer = openDrawer, topAppBarState = topAppBarState)
        },
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //Todo
                }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)

        MainScreenContent(
            modifier = contentModifier,
            onTimeTypeChange = { viewModel.updateCurrentTime(it) },
            currentTimeType = currentTime,
            viewModel = viewModel
        )


    }


}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onTimeTypeChange: (Times) -> Unit,
    currentTimeType: Times,
    viewModel: MainViewModel
) {
    Surface(
        color = SurfaceRed
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            TopButtons(
                onClickButton = { onTimeTypeChange(it) },
                currentTimeType = currentTimeType
            )
            Spacer(modifier = Modifier.height(35.dp))
            TimerBody(viewModel)
            Spacer(modifier = Modifier.height(20.dp))
            FavoriteTasks()

        }

    }
}


@Composable
private fun TopButtons(
    onClickButton: (Times) -> Unit,
    currentTimeType: Times = Times.PomodoroTime()
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            border = BorderStroke(1.dp, light_SurfaceRedContainer),
            colors = if (currentTimeType is Times.LongTime) {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = light_onSurfaceRed,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            } else {
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.LongTime())
            }) {
            Text(
                text = "Long Break",
                color = if (currentTimeType is Times.LongTime) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, light_SurfaceRedContainer),
            colors = if (currentTimeType is Times.Short) {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = light_onSurfaceRed,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            } else {
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.Short())
            }) {
            Text(
                text = "Short Break",
                color = if (currentTimeType is Times.Short) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, light_SurfaceRedContainer),
            colors = if (currentTimeType is Times.PomodoroTime) {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = light_onSurfaceRed,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            } else {
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.PomodoroTime())
            }) {
            Text(
                text = "Pomodoro",
                color = if (currentTimeType is Times.PomodoroTime) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )

        }
    }
}

@Composable
fun TimerBody(viewModel: MainViewModel) {
    val time = viewModel.currentTime
    if (viewModel.timerIsRunning) {
        val workInfo = viewModel.workInfo?.observeAsState()
        val left = workInfo?.value?.progress?.getFloat(
            "Left",
            viewModel.currentTime.getCurrentPercentage()
        )
        left?.let {
            viewModel.updateCurrent(it)
        }
    }
    Box() {
        StatelessTimer(
            value = time.getCurrentPercentage(),
            time = time.toString(),
            textColor = light_onSurfaceRed,
            handleColor = Color.Green,
            inactiveBarColor = Color.White,
            activeBarColor = md_theme_light_tertiary,
            modifier = Modifier.size(230.dp)
        )
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
        ) {

            IconButton(modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(Color.White), onClick = { viewModel.pauseOrPlayTimer() }) {
                Icon(
                    if (viewModel.timerIsRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)

                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            IconButton(modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(Color.White), onClick = { viewModel.restart() }) {
                Icon(
                    Icons.Filled.Refresh, "",
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp)
                )
            }

        }
    }
}


@Composable
fun FavoriteTasks(modifier: Modifier = Modifier) {

    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(4) {
            Spacer(modifier = Modifier.height(8.dp))
            ImportantTask()
        }
    }
}


@Composable
fun ImportantTask(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(Color.White.copy(0.2f)),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
                painter = painterResource(id = R.drawable.dot),
                contentDescription = "Dot"
            )
            Text("Deneme task")
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Dot",
                tint = md_theme_light_tertiary
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
) {
    val title = stringResource(id = R.string.app_name)
    CenterAlignedTopAppBar(
        colors =  TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = { Text(text = title,color =light_onSurfaceRed) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                    tint = light_onSurfaceRed
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Open search */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.menu),
                    tint = light_onSurfaceRed
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainAppBarPreview(){
    MaterialTheme() {
        CenterAlignedTopAppBar(
            colors =  TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            title = { Text("Pomdoro",color =light_onSurfaceRed) },
            navigationIcon = {
                IconButton(onClick = {
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                        tint = light_onSurfaceRed
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Open search */ }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.menu),
                        tint = light_onSurfaceRed
                    )
                }
            }
        )
    }

}
@Preview
@Composable
fun Deneme() {
    val activeTime by remember { mutableStateOf<Times>(Times.Short()) }
    Surface(
        color = SurfaceRed
    ) {
        Row {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    border = BorderStroke(1.dp, light_SurfaceRedContainer),
                    colors = if (activeTime is Times.LongTime) {
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = light_onSurfaceRed,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    onClick = {

                    }) {
                    Text("Long Break")
                }
                OutlinedButton(
                    border = BorderStroke(1.dp, light_SurfaceRedContainer),
                    colors = if (activeTime is Times.Short) {
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = light_onSurfaceRed,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    onClick = {

                    }) {
                    Text("Short Break")
                }
                OutlinedButton(
                    border = BorderStroke(1.dp, light_SurfaceRedContainer),
                    colors = if (activeTime is Times.PomodoroTime) {
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = light_onSurfaceRed,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    onClick = {

                    }) {
                    Text("Pomodoro")
                }
            }
        }
    }


}



