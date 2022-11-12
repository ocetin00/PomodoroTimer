package com.oguzhancetin.pomodoro.screen.main

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkInfo
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import com.oguzhancetin.pomodoro.ui.StatelessTimer
import com.oguzhancetin.pomodoro.ui.theme.*
import com.oguzhancetin.pomodoro.util.Times
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("EnqueueWork", "SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onAddTaskButtonClicked: () -> Unit = {}
) {

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

    Surface(){

        MainScreenContent(
            modifier = modifier,
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
            onItemFinish = { taskItem -> viewModel.updateTaskItem(taskItem) },
            onAddTaskButtonClicked = onAddTaskButtonClicked

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
    onItemFinish: (taskItem: TaskItem) -> Unit = {},
    onAddTaskButtonClicked: () -> Unit,
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
            TimerBody2(
                currentTime = currentTime,
                timerIsRunning = timerIsRunning,
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
            Spacer(modifier = Modifier.height(15.dp))
            FavoriteTasks(
                favoriteTaskItems = favoriteTaskItems,
                onItemFavorite = onItemFavorite,
                onItemFinish = onItemFinish,
                onAddTaskButtonClicked = onAddTaskButtonClicked
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
fun TimerBody2(
    currentTime: Times,
    timerIsRunning: Boolean,
    restart: () -> Unit,
    pauseOrPlayTimer: () -> Unit,
) {

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
    onItemFinish: (taskItem: TaskItem) -> Unit = {},
    onAddTaskButtonClicked: () -> Unit
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
            AddTaskButton(onAddTaskButtonClicked = onAddTaskButtonClicked)
        }


    }
}

@Composable
fun AddTaskButton(
    modifier: Modifier = Modifier,
    onAddTaskButtonClicked: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(Color.White.copy(0.2f)),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .clickable {
                    onAddTaskButtonClicked()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = stringResource(R.string.add_task)
                )
            Spacer(modifier = Modifier.width(10.dp))

            Text("Add Task", color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
    }
}



@Composable
fun FavoriteTask(
    modifier: Modifier = Modifier,
    taskItem: TaskItem,
    onItemFavorite: (taskItem: TaskItem) -> Unit = {},
    onItemFinish: (taskItem: TaskItem) -> Unit = {},
) {


    val song: MediaPlayer =
        MediaPlayer.create(LocalContext.current, com.oguzhancetin.pomodoro.R.raw.done_sound)

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
                song.start()
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

data class ButtonTimes(val long: Long, val short: Long, val pomodoro: Long)


