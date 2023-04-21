@file:OptIn(ExperimentalFoundationApi::class)

package com.oguzhancetin.pomodoro.presentation.screen.task


import android.media.MediaPlayer
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.common.util.removeDetails
import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.data.mapper.toCategory
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme
import java.util.*
import com.oguzhancetin.pomodoro.domain.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigateTaskDetail: (id: UUID?, categoryId: UUID?) -> Unit = { _, _ -> }
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.taskUIState.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    if (showDialog)
        AddCategoryDialog(value = "", setShowDialog = {
            showDialog = it
        }) { categoryName ->
            if (categoryName.isNotBlank()) {
                viewModel.addCategory(
                    Category(
                        UUID.randomUUID(),
                        categoryName
                    )
                )
            }
        }

    Scaffold(
        topBar = {
            MainAppBar(
                currentRoute = "Task", canNavigateBack = true, navigateUp = onBack
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is UIState.Success -> {
                TaskScreenContent(
                    modifier = modifier
                        .padding(innerPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        },
                    taskItems = (uiState as UIState.Success).taskItems ?: listOf(),
                    onAddItem = { taskItem -> viewModel.addTask(taskItem) },
                    onItemFinish = { taskItem -> viewModel.updateTask(taskItem) },
                    onItemFavorite = { taskItem -> viewModel.updateTask(taskItem) },
                    onClickTaskItem = { id ->
                        onNavigateTaskDetail(
                            id,
                            (uiState as UIState.Success).selectedTaskCategory
                        )
                    },
                    onClickAddCategory = { showDialog = true },
                    categories = (uiState as UIState.Success).categories ?: listOf(),
                    onClickSelectedCategory = { categoryId ->
                        viewModel.onChangeSelectedCategory(
                            categoryId
                        )
                    },
                    selectedCategory = (uiState as UIState.Success).selectedTaskCategory,
                    onClickNewTask = {
                        onNavigateTaskDetail(
                            null,
                            (uiState as UIState.Success).selectedTaskCategory
                        )
                    }
                )
            }

            is UIState.Loading -> {}
            is UIState.Error -> {}

        }

    }
}

//TODO: CategoryWith taskı Map e çevir
@Composable
fun Category(
    modifier: Modifier = Modifier,
    onClickAddCategory: () -> Unit,
    categories: List<CategoryWithTask> = listOf(),
    onClickSelectedCategory: (UUID) -> Unit,
    selectedCategory: UUID? = null
) {

    Column(modifier = modifier) {
        Row(modifier = Modifier, horizontalArrangement = Arrangement.Start) {
            Text("Categories", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
        Spacer(Modifier.height(25.dp))
        Row() {
            Spacer(modifier = Modifier.width(20.dp))
            LazyRow(modifier = Modifier.padding(), content = {
                items(categories) {
                    val taskItemModifier = if (selectedCategory == it.category.id) {
                        Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = MaterialTheme.shapes.large
                        )
                    } else {
                        Modifier
                    }

                    TaskCategoryItem(
                        modifier = taskItemModifier
                            .width(150.dp)
                            .height(100.dp),
                        category = it.category.toCategory(),
                        taskCount = it.taskList.size,
                        onClickSelectedCategory = onClickSelectedCategory
                    )
                    Spacer(modifier = Modifier.width(10.dp))


                }

                item() {
                    Spacer(modifier = Modifier.width(10.dp))
                    AddTaskCategoryItem(modifier = Modifier
                        .width(150.dp)
                        .height(100.dp)
                        .padding(vertical = 5.dp),
                        onClickAddCategory = { onClickAddCategory() })
                }
            })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskCategoryItem(modifier: Modifier = Modifier, onClickAddCategory: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        onClick = {
            onClickAddCategory()
        }
    ) {
        Row(
            modifier = modifier.padding(start = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(
                    text = "Add Category",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    )
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    taskCount: Int,
    onClickSelectedCategory: (UUID) -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        onClick = { onClickSelectedCategory(category.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = category.name,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$taskCount Task",
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
    onItemFavorite: (taskItem: TaskItem) -> Unit = {},
    onClickTaskItem: (id: UUID) -> Unit = { },
    onClickAddCategory: () -> Unit,
    categories: List<CategoryWithTask> = listOf(),
    onClickSelectedCategory: (UUID) -> Unit,
    selectedCategory: UUID? = null,
    onClickNewTask: () -> Unit = {}
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
                Category(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onClickAddCategory = { onClickAddCategory() },
                    categories = categories,
                    onClickSelectedCategory = onClickSelectedCategory,
                    selectedCategory = selectedCategory
                )
                Spacer(Modifier.height(25.dp))
                TaskListBody(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .fillMaxHeight(),
                    onClickNewTask = onClickNewTask,
                    taskList = taskItems
                )
                /*TaskItemAdd(
                    Modifier.padding(horizontal = 5.dp),
                    onAddItem = { taskItem ->
                        onAddItem(taskItem)
                    }
                )*/
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemContent(
    taskItem: TaskItem, modifier: Modifier = Modifier,
    onItemFinish: (taskItem: TaskItem) -> Unit,
    onItemFavorite: (taskItem: TaskItem) -> Unit,
    onTaskItemClick: (id: UUID) -> Unit = {},
) {
    val song: MediaPlayer =
        MediaPlayer.create(LocalContext.current, com.oguzhancetin.pomodoro.R.raw.done_sound)

    val calendar: Calendar = Calendar.getInstance()
    calendar.removeDetails()
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.padding(horizontal = 10.dp),
        onClick = {
            onTaskItemClick(taskItem.id)
        }
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

@Preview
@Composable
fun TaskListBodyPreview() {

    val taskList = listOf(
        TaskItem(description = "Task1", id = UUID.randomUUID()),
        TaskItem(description = "Task2", id = UUID.randomUUID(), doneDate = 12321),
        TaskItem(description = "Task3", id = UUID.randomUUID()),
        TaskItem(description = "Task4", id = UUID.randomUUID()),
        TaskItem(description = "Task5", id = UUID.randomUUID(), doneDate = 12321)
    )

    PomodoroTheme {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            TaskListBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(300.dp),
                taskList = taskList,
                taskTitle = "Default Task List"
            )
        }
    }
}

@Composable
fun TaskListBody(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),
    taskTitle: String = "Task",
    taskList: List<TaskItem> = emptyList(),
    onClickNewTask: () -> Unit = {}
) {

    Column(modifier = modifier) {
        Row(modifier = Modifier) {
            Text(taskTitle, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
        Spacer(Modifier.height(25.dp))
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        ) {
            Row() {
                Column() {
                    TaskBodyHeader(onClickNewTask = onClickNewTask)
                    taskList.forEach { taskItem ->
                        TaskBodyItem(taskItem)
                    }
                }
            }
        }
    }

}

@Composable
fun TaskBodyHeader(modifier: Modifier = Modifier, onClickNewTask: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onClickNewTask() }) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                "add task",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            "Add New Task", style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            )
        )
    }
}

@Composable
fun TaskBodyItem(taskItem: TaskItem) {
    taskItem.doneDate?.let {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    "add task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                taskItem.description?: "", style = TextStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            )
        }
    } ?: run {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.RadioButtonUnchecked,
                    "add task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                taskItem.description?: " ", style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            )
        }
    }

}

