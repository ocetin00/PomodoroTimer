package com.oguzhancetin.pomodoro.screen.main

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkInfo
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import com.oguzhancetin.pomodoro.ui.StatelessTimer
import com.oguzhancetin.pomodoro.ui.theme.*
import com.oguzhancetin.pomodoro.util.Time.WorkUtil
import com.oguzhancetin.pomodoro.util.Times
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onAddTaskButtonClicked: () -> Unit = {}
) {

    val uiState by viewModel.mainUiState.collectAsStateWithLifecycle()
    val long = uiState.timePreferencesState.long
    val short = uiState.timePreferencesState.short
    val pomodoro = uiState.timePreferencesState.pomodoro



    Surface() {

        if (long != null && short != null && pomodoro != null) {
            MainScreenContent(
                modifier = modifier,
                onTimeTypeChange = { viewModel.updateCurrentTime(it) },
                left = uiState.leftTime,
                selectedTimeType = uiState.runningTimeType,
                buttonTimes = ButtonTimes(
                    pomodoro = pomodoro,
                    long = long,
                    short = short
                ),
                progress = uiState.timeProgress,
                timerIsRunning = uiState.timerIsRunning,
                pauseOrPlayTimer = { viewModel.pauseOrPlayTimer() },
                restart = { viewModel.restart() },
                favoriteTaskItems = uiState.favouriteTasks,
                onItemFavorite = { taskItem -> viewModel.updateTask(taskItem = taskItem) },
                onItemFinish = { taskItem -> viewModel.updateTaskItem(taskItem) },
                onAddTaskButtonClicked = onAddTaskButtonClicked

            )
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
            //  .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TopButtons(
                onClickButton = { onTimeTypeChange(it) },
                currentTimeType = selectedTimeType,
                buttonTimes = buttonTimes
            )
            Spacer(modifier = Modifier.height(35.dp))
            TimerBody2(
                left = left,
                progress = progress,
                timerIsRunning = timerIsRunning,
                restart = restart,
                pauseOrPlayTimer = pauseOrPlayTimer
            )

            Spacer(modifier = Modifier.height(20.dp))


            if (selectedTimeType !is Times.Pomodoro) {
                BreakBody(time = selectedTimeType)
            } else {
                FavoriteTasks(
                    favoriteTaskItems = favoriteTaskItems,
                    onItemFavorite = onItemFavorite,
                    onItemFinish = onItemFinish,
                    onAddTaskButtonClicked = onAddTaskButtonClicked
                )
            }


        }

    }
}

@Composable
private fun TopButtons(
    buttonTimes: ButtonTimes,
    onClickButton: (Times) -> Unit,
    currentTimeType: Times
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

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
                onClickButton(Times.Pomodoro().also { it.time = buttonTimes.pomodoro })
            }) {
            Text(
                text = "Pomodoro",
                color = if (currentTimeType is Times.Pomodoro) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
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
                onClickButton(Times.Short().also { it.time = buttonTimes.short })
            }) {
            Text(
                text = "Short Break",
                color = if (currentTimeType is Times.Short) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )
        }
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
                onClickButton(Times.Long().also { it.time = buttonTimes.long })
            }) {
            Text(
                text = "Long Break",
                color = if (currentTimeType is Times.Long) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )
        }

    }
}

@Preview
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
    LazyColumn(
        modifier
            .fillMaxWidth(0.7f)
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (favoriteTaskItems.isNotEmpty()) {
            items(
                items = favoriteTaskItems,
                key = { task -> task.id }
            ) { task ->
                Spacer(modifier = Modifier.height(8.dp))

                FavoriteTask(
                    modifier = Modifier,
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

@Composable
fun AddTaskButton(
    modifier: Modifier = Modifier,
    onAddTaskButtonClicked: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(Color.White.copy(0.2f)),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 15.dp, vertical = 10.dp)
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

            Text("Add Task", color = MaterialTheme.colorScheme.onPrimary)
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
                .height(50.dp)
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

