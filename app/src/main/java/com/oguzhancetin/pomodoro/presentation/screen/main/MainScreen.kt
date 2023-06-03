@file:OptIn(ExperimentalMaterialApi::class)

package com.oguzhancetin.pomodoro.presentation.screen.main

import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.common.util.removeDetails
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.presentation.ui.AppDrawer
import com.oguzhancetin.pomodoro.presentation.ui.PomodoroDestinations
import com.oguzhancetin.pomodoro.presentation.ui.PomodoroNavigationActions
import com.oguzhancetin.pomodoro.presentation.ui.StatelessTimer
import com.oguzhancetin.pomodoro.presentation.screen.util.ToggleTab
import com.oguzhancetin.pomodoro.ui.*
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.common.util.withNotNull
import com.oguzhancetin.pomodoro.presentation.ui.theme.*
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
    onAddTaskButtonClicked: () -> Unit = {}
) {

    val uiState by viewModel.mainUiState.collectAsStateWithLifecycle()
    val favoriteTaskUiState by viewModel.favoriteTaskUiState.collectAsStateWithLifecycle()
    val long = uiState.timePreferencesState.long
    val short = uiState.timePreferencesState.short
    val pomodoro = uiState.timePreferencesState.pomodoro

    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Pomodoro", "Task", "Chart", "Setting")


    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val navigationActions = remember(navController) {
        PomodoroNavigationActions(navController)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = PomodoroDestinations.MAIN_ROUTE,
                navigateToMain = {
                    scope.launch {
                        drawerState.close()
                        navigationActions.navigateToMain.invoke()
                    }

                },
                navigateToSetting = {
                    scope.launch {
                        drawerState.close()
                        navigationActions.navigateToSetting.invoke()
                    }

                },
                navigateToStatus = {
                    scope.launch {
                        drawerState.close()
                        navigationActions.navigateToStatus.invoke()
                    }
                },
                navigateToTask = {
                    scope.launch {
                        drawerState.close()
                        navigationActions.navigateToTask.invoke()
                    }


                },
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    MainAppBar(
                        currentRoute = "Pomodoro",
                        openDrawer = { scope.launch { drawerState.open() } })
                }
            ) { innerPadding ->
                Surface() {
                    MainScreenContent(
                        modifier = modifier.padding(innerPadding),
                        onTimeTypeChange = { timeType ->
                            withNotNull(long, short, pomodoro) { long, short, pomodoro ->
                                viewModel.updateCurrentTime(
                                    timeType.also {
                                        when (it) {
                                            is Times.Long -> it.time = long
                                            is Times.Short -> it.time = short
                                            is Times.Pomodoro -> it.time = pomodoro
                                            else -> {}
                                        }
                                    }
                                )
                            }
                        },
                        left = uiState.leftTime,
                        selectedTimeType = uiState.runningTimeType,
                        progress = uiState.timeProgress,
                        timerIsRunning = uiState.timerIsRunning,
                        pauseOrPlayTimer = { viewModel.pauseOrPlayTimer() },
                        restart = { viewModel.restart() },
                        favoriteTaskItems = favoriteTaskUiState.favouriteTasks,
                        onItemFavorite = { taskItem -> viewModel.updateTask(taskItem = taskItem) },
                        onItemFinish = { taskItem -> viewModel.onDoneTask(taskItem) },
                        onAddTaskButtonClicked = onAddTaskButtonClicked,
                    )
                }
            }

        }
    )


}

@Composable
fun SheetContent(
    favoriteTaskItems: List<TaskItemEntity>,
    onItemFavorite: (taskItem: TaskItemEntity) -> Unit,
    onItemFinish: (taskItem: TaskItemEntity) -> Unit = {},
    onAddTaskButtonClicked: () -> Unit,
) {

    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.height(30.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(ShapeDefaults.ExtraLarge)
                    .height(8.dp)
                    .width(65.dp)
                    .background(Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (favoriteTaskItems.isNotEmpty()) {
                items(
                    items = favoriteTaskItems,
                    key = { task -> task.id }
                ) { task ->
                    Spacer(modifier = Modifier.height(8.dp))

                    FavoriteTask(
                        modifier = Modifier.fillMaxSize(),
                        taskItem = task,
                        onItemFavorite = onItemFavorite,
                        onItemFinish = onItemFinish
                    )
                }
            } else {
                item {
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    AddTaskButton(onAddTaskButtonClicked = onAddTaskButtonClicked)
                }
            }
        }
    }

}

@Composable
fun BreakBody(time: Times) {
    if (time is Times.Long) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "How About Some Relaxation?",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 1f)
            )

        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.long_break), "long", alpha = 0.5f)
        }
    } else if (time is Times.Short) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "How About A Coffee Break?",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 1f)
            )

        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.short_break), "long", alpha = 0.5f)
        }
    }

}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onTimeTypeChange: (Times) -> Unit,
    left: String,
    progress: Float,
    selectedTimeType: Times,
    timerIsRunning: Boolean,
    restart: () -> Unit,
    pauseOrPlayTimer: () -> Unit,
    favoriteTaskItems: List<TaskItemEntity>,
    onItemFavorite: (taskItem: TaskItemEntity) -> Unit,
    onItemFinish: (taskItem: TaskItemEntity) -> Unit = {},
    onAddTaskButtonClicked: () -> Unit,
) {


    Surface(
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
            //  .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TopButtons(
                onClickButton = { onTimeTypeChange(it) },
                currentTimeType = selectedTimeType,
            )
            Spacer(modifier = Modifier.height(35.dp))
            TimerBody2(
                left = left,
                progress = progress,
                timerIsRunning = timerIsRunning,
                restart = restart,
                pauseOrPlayTimer = pauseOrPlayTimer
            )
            Spacer(modifier = Modifier.height(30.dp))


            if (selectedTimeType !is Times.Pomodoro) {
                BreakBody(time = selectedTimeType)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (favoriteTaskItems.isNotEmpty()) {
                        items(
                            items = favoriteTaskItems,
                            key = { task -> task.id }
                        ) { task ->
                            Spacer(modifier = Modifier.height(8.dp))

                            FavoriteTask(
                                modifier = Modifier.fillMaxSize(),
                                taskItem = task,
                                onItemFavorite = onItemFavorite,
                                onItemFinish = onItemFinish
                            )
                        }
                    } else {
                        item {
                            AddTaskButton(onAddTaskButtonClicked = onAddTaskButtonClicked)
                        }
                    }
                }
            }


        }
    }
}

@Composable
private fun TopButtons(
    onClickButton: (Times) -> Unit,
    currentTimeType: Times
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val currentTimeTypeIndex = when (currentTimeType) {
            is Times.Long -> 0
            is Times.Short -> 1
            is Times.Pomodoro -> 2
            else -> 0
        }
        ToggleTab(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            selectedColor = MaterialTheme.colorScheme.onPrimaryContainer,
            tabItemTextStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
            initialSelectedTabIndex = currentTimeTypeIndex,
            titleList = listOf("Long", "Short", "Pomodoro"),
            onTabSelected = {
                when (it) {
                    0 -> onClickButton(Times.Long())
                    1 -> onClickButton(Times.Short())
                    2 -> onClickButton(Times.Pomodoro())
                }
            })
    }
}

@Composable
fun TimerBody2(
    left: String = "25 : 00",
    progress: Float = 1f,
    timerIsRunning: Boolean = false,
    restart: () -> Unit = {},
    pauseOrPlayTimer: () -> Unit = {},
) {

    Box() {
        StatelessTimer(
            value = progress,
            time = left,
            textColor = MaterialTheme.colorScheme.primary,
            handleColor = Color.Green,
            inactiveBarColor = light_RedBackgroundContainer,
            activeBarColor = Color.White,
            modifier = Modifier.size(300.dp)
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center,
        ) {

            IconButton(modifier = Modifier, onClick = { pauseOrPlayTimer() }) {
                Icon(
                    painter = if (timerIsRunning) {
                        painterResource(id = R.drawable.pause_fill)
                    } else {
                        painterResource(id = R.drawable.play_arrow)
                    },
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)

                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(modifier = Modifier,
                onClick = { restart() })
            {
                Icon(
                    Icons.Filled.Refresh, "",
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp)
                )
            }

        }

    }
}


@Composable
fun AddTaskButton(
    modifier: Modifier = Modifier,
    onAddTaskButtonClicked: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(Color.White.copy(0.4f)),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .clickable {
                    onAddTaskButtonClicked()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = stringResource(R.string.add_task)
            )
            Spacer(modifier = Modifier.width(30.dp))

            Text("Add Task", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun FavoriteTask(
    modifier: Modifier = Modifier,
    taskItem: TaskItemEntity,
    onItemFavorite: (taskItem: TaskItemEntity) -> Unit = {},
    onItemFinish: (taskItem: TaskItemEntity) -> Unit = {},
) {

    val song: MediaPlayer =
        MediaPlayer.create(LocalContext.current, com.oguzhancetin.pomodoro.R.raw.done_sound)

    Card(
        colors = CardDefaults.cardColors(Color.White.copy(0.4f)),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .height(50.dp)
                .padding(horizontal = 30.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = {
                song.start()
                onItemFinish(taskItem.copy(isFinished = true))

            }) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    painter = painterResource(id = R.drawable.radio_button_unchecked),
                    contentDescription = stringResource(R.string.add_task)
                )
            }


            Text(
                text = taskItem.description ?: "",
                modifier = Modifier.padding(horizontal = 3.dp),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                )
            )
            IconButton(onClick = {
                onItemFavorite(taskItem.copy(isFavorite = !taskItem.isFavorite))
            }) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    painter = if (taskItem.isFavorite) {
                        painterResource(id = R.drawable.grade)
                    } else {
                        painterResource(id = R.drawable.grade_fill0)
                    },
                    contentDescription = stringResource(R.string.add_task)
                )
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    openDrawer: () -> Unit = {},
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    canNavigateBack: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState),
    navigateUp: () -> Unit = {},
    currentRoute: String
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = { Text(text = currentRoute, color = light_onRedBackground) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
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

