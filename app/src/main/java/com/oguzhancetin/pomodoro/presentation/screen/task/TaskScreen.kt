@file:OptIn(ExperimentalFoundationApi::class)

package com.oguzhancetin.pomodoro.presentation.screen.task


import android.media.MediaPlayer
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.common.util.removeDetails
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme
import java.util.*
import java.util.Locale.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.taskUIState.collectAsStateWithLifecycle()
    val tasks = uiState.taskItems


    Scaffold(
        topBar = {
            MainAppBar(
                currentRoute = "Task", canNavigateBack = true, navigateUp = onBack
            )
        }
    ) {
        if (!uiState.isLoading) {
            TaskScreenContent(
                modifier = modifier
                    .padding(it)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    },
                taskItems = tasks,
                onAddItem = { taskItem -> viewModel.addTask(taskItem) },
                onItemFinish = { taskItem -> viewModel.updateTask(taskItem) },
                onItemFavorite = { taskItem -> viewModel.updateTask(taskItem) }
            )
        }

    }
}

@Composable
fun Category(modifier:Modifier = Modifier) {

    Column (modifier = modifier){
        Row(modifier = Modifier, horizontalArrangement = Arrangement.Start) {
            Text("Categories", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
        Spacer(Modifier.height(25.dp))
        LazyRow(content = {
            items(10) {
                TaskCategoryItem(
                    modifier = Modifier
                        .width(150.dp)
                        .padding(vertical = 5.dp),
                    title = "Task $it",
                    taskCount = it
                )
                Spacer(modifier = Modifier.width(15.dp))
            }
        })
    }
}

@Composable
@Preview
fun TaskCategoryPreview() {
    PomodoroTheme {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
           Category(modifier = Modifier.padding(horizontal = 10.dp))
        }
    }
}

@Composable
fun TaskCategoryItem(modifier: Modifier = Modifier, title: String, taskCount: Int) {
    Card(shape = MaterialTheme.shapes.large,      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),) {
        Row(modifier = modifier.padding(start = 10.dp, top = 8.dp,bottom = 8.dp)) {
            Column() {
                Text(
                    text = title,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "20 Task",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    )
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskScreenContent(
    modifier: Modifier = Modifier,
    taskItems: List<TaskItem> = listOf<TaskItem>(),
    onAddItem: (taskItem: TaskItem) -> Unit = {},
    onItemFinish: (taskItem: TaskItem) -> Unit = {},
    onItemFavorite: (taskItem: TaskItem) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 5.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Category(modifier = Modifier.padding(horizontal = 10.dp))
                /*TaskItemAdd(
                    Modifier.padding(horizontal = 5.dp),
                    onAddItem = { taskItem ->
                        onAddItem(taskItem)
                    }
                )*/
            }

            items(taskItems, { item: TaskItem -> item.id }) { item ->
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                        .padding(horizontal = 10.dp)
                )
                TaskItemContent(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .animateItemPlacement(),
                    taskItem = item,
                    onItemFavorite = { taskItem -> onItemFavorite(taskItem) },
                    onItemFinish = { taskItem -> onItemFinish(taskItem) }
                )

            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemAdd(modifier: Modifier = Modifier, onAddItem: (taskItem: TaskItem) -> Unit = {}) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        var text by remember { mutableStateOf(TextFieldValue("")) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(paddingValues = PaddingValues(horizontal = 20.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                if (text.text.isNotBlank()) {
                    onAddItem(
                        TaskItem(
                            id = UUID.randomUUID(),
                            description = text.text,
                            createdDate = System.currentTimeMillis(),
                            isFavorite = false,
                            doneDate = null
                        )
                    )
                }
                text = TextFieldValue()
            }) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = stringResource(R.string.add_task)
                )
            }

            val textFieldModifier = Modifier
                .fillMaxWidth()
            OutlinedTextField(
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = textFieldModifier,
                value = text,
                onValueChange = {
                    if (it.text.length <= 23) text = it
                },
                placeholder = {
                    Text(text = "Add a task")
                },
                trailingIcon = {
                    if (text.text.isNotBlank()) {
                        Icon(Icons.Default.Clear,
                            contentDescription = "clear text",
                            modifier = Modifier
                                .clickable {
                                    text = TextFieldValue("")
                                }
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    errorBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (text.text.isNotBlank()) {
                            onAddItem(
                                TaskItem(
                                    id = UUID.randomUUID(),
                                    description = text.text,
                                    createdDate = System.currentTimeMillis(),
                                    isFavorite = false,
                                    doneDate = null
                                )
                            )
                        }
                    }),
            )
            IconButton(onClick = {
                text = TextFieldValue("")
            }) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        }
    }
}

@Composable
fun TaskItemContent(
    taskItem: TaskItem, modifier: Modifier = Modifier,
    onItemFinish: (taskItem: TaskItem) -> Unit,
    onItemFavorite: (taskItem: TaskItem) -> Unit
) {
    val song: MediaPlayer =
        MediaPlayer.create(LocalContext.current, com.oguzhancetin.pomodoro.R.raw.done_sound)

    val calendar: Calendar = Calendar.getInstance()
    calendar.removeDetails()
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(paddingValues = PaddingValues(horizontal = 20.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                song.start()
                onItemFinish(taskItem.copy(isFinished = true, doneDate = calendar.timeInMillis))
            }) {
                Icon(
                    imageVector = Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
            Text(text = taskItem.description ?: "")
            IconButton(onClick = {
                onItemFavorite(taskItem.copy(isFavorite = !taskItem.isFavorite))
            }) {
                Icon(
                    imageVector = if (taskItem.isFavorite) Icons.Filled.Star else Icons.Outlined.Grade,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        }
    }
}

