package com.oguzhancetin.pomodoro.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
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

    val topAppBarState = rememberTopAppBarState()

    val long = viewModel.longTime.collectAsState(initial = Times.Long.time)
    val short = viewModel.shortTime.collectAsState(initial = Times.Short.time)
    val pomodoro = viewModel.pomodoroTime.collectAsState(initial = Times.Pomodoro.time)

    val currentSelectedTime = viewModel.currentTime

    val favoriteTaskItems by viewModel.favoriteTaskItems.collectAsState(initial = listOf())

    /**
     * if settings was changed new value update
     */
    when (currentSelectedTime.value) {
        Times.Pomodoro -> {
            val pTime = Times.Pomodoro.apply { time = pomodoro.value;left = pomodoro.value }
            viewModel.updateCurrentTime(pTime)
        }
        Times.Short -> {
            viewModel.updateCurrentTime(Times.Short.also {
                it.time = short.value;it.left = short.value
            })
        }
        Times.Long -> {
            viewModel.updateCurrentTime(Times.Long.also {
                it.time = long.value;it.left = long.value
            })
        }
    }


    Scaffold(
        topBar = {
            MainAppBar(openDrawer = openDrawer, topAppBarState = topAppBarState)
        },
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {

                ExtendedFloatingActionButton(
                    onClick = {
                        //TODO: navigate task screen
                    }) {
                    Row(horizontalArrangement = Arrangement.SpaceAround){
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Add")
                        Text("Task")
                    }

                }

        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)


            MainScreenContent(
                modifier = contentModifier,
                onTimeTypeChange = { viewModel.updateCurrentTime(it) },
                currentTimeType = currentSelectedTime.value,
                buttonTimes = ButtonTimes(
                    pomodoro = pomodoro.value,
                    long = long.value,
                    short = short.value
                ),
                currentTime = currentSelectedTime.value,
                updateCurrent = { viewModel.updateCurrentLeft(it) },
                timerIsRunning = viewModel.timerIsRunning,
                workInfo = viewModel.workInfo?.observeAsState()?.value,
                pauseOrPlayTimer = { viewModel.pauseOrPlayTimer() },
                restart = { viewModel.restart() },
                favoriteTaskItems = favoriteTaskItems,
                onItemFavorite = { taskItem -> viewModel.updateTask(taskItem = taskItem) },
                onItemFinish = { taskItem -> viewModel.updateTaskItem(taskItem) }

            )

        }



}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onTimeTypeChange: (Times) -> Unit,
    currentTimeType: Times,
    currentTime: Times,
    timerIsRunning: Boolean,
    updateCurrent: (left: Float) -> Unit,
    workInfo: WorkInfo?,
    restart: () -> Unit,
    pauseOrPlayTimer: () -> Unit,
    buttonTimes: ButtonTimes,
    favoriteTaskItems: List<TaskItem>,
    onItemFavorite: (taskItem: TaskItem) -> Unit,
    onItemFinish: (taskItem: TaskItem) -> Unit = {}
) {
    Surface(
        color = RedBackground
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            TopButtons(
                onClickButton = { onTimeTypeChange(it) },
                currentTimeType = currentTimeType,
                buttonTimes = buttonTimes
            )
            Spacer(modifier = Modifier.height(35.dp))
            TimerBody(
                currentTime = currentTime,
                timerIsRunning = timerIsRunning,
                updateCurrent = updateCurrent,
                workInfo = workInfo,
                restart = restart,
                pauseOrPlayTimer = pauseOrPlayTimer
            )
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Manage your time well!",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 1f)
                )

            }
            Spacer(modifier = Modifier.width(15.dp))
            FavoriteTasks(
                favoriteTaskItems = favoriteTaskItems,
                onItemFavorite = onItemFavorite,
                onItemFinish = onItemFinish
            )

        }

    }
}


@Composable
private fun TopButtons(
    buttonTimes: ButtonTimes,
    onClickButton: (Times) -> Unit,
    currentTimeType: Times = Times.Pomodoro
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            border = BorderStroke(1.dp, light_RedBackgroundContainer),
            colors = if (currentTimeType is Times.Long) {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = light_onRedBackground,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            } else {
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.Long.also { it.time = buttonTimes.long })
            }) {
            Text(
                text = "Long Break",
                color = if (currentTimeType is Times.Long) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, light_RedBackgroundContainer),
            colors = if (currentTimeType is Times.Short) {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = light_onRedBackground,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            } else {
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.Short.also { it.time = buttonTimes.short })
            }) {
            Text(
                text = "Short Break",
                color = if (currentTimeType is Times.Short) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )
        }
        OutlinedButton(
            border = BorderStroke(1.dp, light_RedBackgroundContainer),
            colors = if (currentTimeType is Times.Pomodoro) {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = light_onRedBackground,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            } else {
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.Pomodoro.also { it.time = buttonTimes.pomodoro })
            }) {
            Text(
                text = "Pomodoro",
                color = if (currentTimeType is Times.Pomodoro) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )

        }
    }
}


@Composable
fun TimerBody(
    currentTime: Times,
    timerIsRunning: Boolean,
    updateCurrent: (left: Float) -> Unit,
    workInfo: WorkInfo?,
    restart: () -> Unit,
    pauseOrPlayTimer: () -> Unit
) {
    if (timerIsRunning) {
        val left = workInfo?.progress?.getFloat(
            "Left",
            currentTime.getCurrentPercentage()
        )
        left?.let {
            updateCurrent(it)
        }
    }
    Box() {
        StatelessTimer(
            value = currentTime.getCurrentPercentage(),
            time = currentTime.toString(),
            textColor = md_theme_light_onPrimary,
            handleColor = Color.Green,
            inactiveBarColor = light_RedBackgroundContainer,
            activeBarColor = Color.White,
            modifier = Modifier.size(230.dp)
        )

        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
        ) {

            IconButton(modifier = Modifier
                .shadow(10.dp, MaterialTheme.shapes.large)
                .clip(MaterialTheme.shapes.large)
                .background(Color.White), onClick = { pauseOrPlayTimer() }) {
                Icon(
                    if (timerIsRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)

                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(modifier = Modifier
                .shadow(10.dp, MaterialTheme.shapes.large)
                .clip(MaterialTheme.shapes.large)
                .background(Color.White), onClick = { restart() })
            {
                Icon(
                    Icons.Filled.Refresh, "",
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp)
                )
            }

        }

    }
}


@Composable
fun FavoriteTasks(
    modifier: Modifier = Modifier,
    favoriteTaskItems: List<TaskItem>,
    onItemFavorite: (taskItem: TaskItem) -> Unit,
    onItemFinish: (taskItem: TaskItem) -> Unit = {}
) {
    Column(
        modifier
            .fillMaxWidth(0.7f)
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (favoriteTaskItems.isNotEmpty()) {

            favoriteTaskItems.forEach {
                Spacer(modifier = Modifier.height(8.dp))
                FavoriteTask(
                    modifier = Modifier,
                    taskItem = it,
                    onItemFavorite = onItemFavorite,
                    onItemFinish = onItemFinish
                )
            }
            Spacer(modifier = Modifier.height(60.dp))
        } else {
            AddTaskButton()
        }


    }
}

@Composable
fun AddTaskButton(
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(Color.White.copy(0.2f)),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
            Text("Task Ekle")
        }
    }
}

@Composable
fun FavoriteTask(
    modifier: Modifier = Modifier,
    taskItem: TaskItem,
    onItemFavorite: (taskItem: TaskItem) -> Unit = {},
    onItemFinish: (taskItem: TaskItem) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(light_task_color.copy(alpha = 0.2f)),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = {

                onItemFinish(taskItem.copy(isFinished = true))
            }) {
                Icon(
                    tint = light_onRedBackground,
                    imageVector = Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = stringResource(R.string.add_task)
                )
            }


            Text(
                text = taskItem.description ?: "",
                modifier = Modifier.padding(horizontal = 3.dp),
                style = TextStyle(
                    color = light_onRedBackground,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                )
            )
            IconButton(onClick = {
                onItemFavorite(taskItem.copy(isFavorite = !taskItem.isFavorite))
            }) {
                Icon(
                    tint = light_onRedBackground,
                    imageVector = if (taskItem.isFavorite) Icons.Filled.Star else Icons.Outlined.Grade,
                    contentDescription = stringResource(R.string.add_task)
                )
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
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
) {
    val title = stringResource(id = R.string.app_name)
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = { Text(text = title, color = light_onRedBackground) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                    tint = light_onRedBackground
                )
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


data class ButtonTimes(val long: Long, val short: Long, val pomodoro: Long)


