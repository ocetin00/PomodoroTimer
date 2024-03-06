@file:OptIn(ExperimentalFoundationApi::class)

package com.oguzhancetin.pomodoro.presentation.screen.task


import android.media.MediaPlayer
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.presentation.common.MainAppBar
import com.oguzhancetin.pomodoro.core.util.removeDetails
import com.oguzhancetin.pomodoro.core.database.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.core.mapper.toCategory
import com.oguzhancetin.pomodoro.core.model.TaskItem
import com.oguzhancetin.pomodoro.presentation.theme.PomodoroTheme
import java.util.*
import com.oguzhancetin.pomodoro.core.model.Category
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onNavigateTaskDetail: (id: String?, categoryId: UUID?) -> Unit = { _, _ -> }
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.taskUIState.collectAsStateWithLifecycle()

    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    when (val dialogState = dialogState) {
        is DialogState.ShowDialog -> {
            val category = dialogState.category

            CategoryDialog(
                modifier = Modifier,
                category = category,
                onDismissRequest = { c ->
                    c?.let { viewModel.upsertCategory(it) }
                    viewModel.dismisDialog()
                },
                onDeleteClick = { c ->
                    c?.let { viewModel.deleteCategory(it) }
                    viewModel.dismisDialog()
                }
            )

        }

        DialogState.DismissDialog -> {

        }
    }


    Scaffold(
        topBar = {
            MainAppBar(
                currentRoute = "Task", canNavigateBack = true, navigateUp = onBack
            )
        }
    ) { innerPadding ->
        val state = uiState
        when (state) {
            is UIState.Loading -> {

            }

            is UIState.Success -> {
                TaskScreenContent(
                    modifier = modifier
                        .padding(innerPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        },
                    taskItems = state.taskItems ?: listOf(),
                    onAddItem = { taskItem -> viewModel.addTask(taskItem) },
                    onItemFinish = { taskItem -> viewModel.updateTask(taskItem) },
                    onItemFavorite = { taskItem -> viewModel.updateTask(taskItem) },
                    onClickTaskItem = { id ->
                        onNavigateTaskDetail(
                            id.toString(),
                            state.selectedTaskCategory
                        )
                    },
                    onCategoryLongClick = { category ->
                        viewModel.showDialog(category)
                    },
                    categories = state.categories ?: listOf(),
                    onClickSelectedCategory = { categoryId ->
                        viewModel.onChangeSelectedCategory(
                            categoryId
                        )
                    },
                    selectedCategory = state.selectedTaskCategory,
                    onClickNewTask = {
                        onNavigateTaskDetail(
                            "",
                            state.selectedTaskCategory
                        )
                    },

                    onCategoryAddClick = {
                        viewModel.showCategoryDialog()
                    },
                    onClearListClick = {
                        viewModel.clearList()
                    }

                )
            }

            is UIState.Error -> {

            }

        }

    }

}

//TODO: CategoryWith taskı Map e çevir
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Category(
    modifier: Modifier = Modifier,
    onClickAddCategory: () -> Unit,
    categories: List<CategoryWithTask> = listOf(),
    onClickSelectedCategory: (UUID) -> Unit,
    selectedCategory: UUID? = null,
    openCategoryDetail: (Category) -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Column(modifier = modifier) {
        Row(modifier = Modifier, horizontalArrangement = Arrangement.Start) {
            Text("Categories", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
        Spacer(Modifier.height(25.dp))
        Row() {
            LazyRow(modifier = Modifier.padding(), content = {
                itemsIndexed(categories) { index, item ->
                    val taskItemModifier = if (selectedCategory == item.category.id) {
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
                            .combinedClickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    onClickSelectedCategory(item.category.id)
                                },
                                onLongClick = {
                                    openCategoryDetail(item.category.toCategory())
                                }
                            )
                            .width(150.dp)
                            .height(100.dp),
                        category = item.category.toCategory(),
                        taskCount = item.taskList.size,
                        onClickSelectedCategory = onClickSelectedCategory,
                        isFirstItem = index == 0
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
    onClickSelectedCategory: (UUID) -> Unit,
    isFirstItem: Boolean = false,
) {

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
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
                    overflow = TextOverflow.Ellipsis,
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
    onClickTaskItem: (id: Long) -> Unit = { },
    onCategoryAddClick: () -> Unit = {},
    onCategoryLongClick: (Category) -> Unit,
    categories: List<CategoryWithTask> = listOf(),
    onClickSelectedCategory: (UUID) -> Unit,
    selectedCategory: UUID? = null,
    onClickNewTask: () -> Unit = {},
    onClearListClick: () -> Unit = {}
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 5.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Category(
                modifier = Modifier.padding(horizontal = 20.dp),
                onClickAddCategory = { onCategoryAddClick() },
                categories = categories,
                onClickSelectedCategory = onClickSelectedCategory,
                selectedCategory = selectedCategory,
                openCategoryDetail = onCategoryLongClick
            )
            Spacer(Modifier.height(25.dp))
            Row(
                modifier = Modifier.padding(start = 20.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Task", fontSize = MaterialTheme.typography.titleMedium.fontSize)
            }
            Spacer(Modifier.height(25.dp))
            TaskListBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .weight(1f),
                onClickNewTask = onClickNewTask,
                taskList = taskItems,
                onItemFavorite = onItemFavorite,
                onItemFinish = onItemFinish,
                onClickTaskItem = onClickTaskItem,
                onClearListClick = onClearListClick
            )
            Spacer(Modifier.height(10.dp))

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
                            id = 1,//UUID.randomUUID(),
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
                                    id = 1,// UUID.randomUUID(),
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
    onTaskItemClick: (id: Long) -> Unit = {},
) {
    val song: MediaPlayer =
        MediaPlayer.create(LocalContext.current, com.oguzhancetin.pomodoro.R.raw.done_sound)

    val calendar = GregorianCalendar(TimeZone.getDefault())
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
        TaskItem(description = "Task1", id = 1, isFinished = true),
        TaskItem(description = "Task2", id = 1, doneDate = 12321),
        TaskItem(description = "Task3", id = 1),

        TaskItem(description = "Task5", id = 1, doneDate = 12321)
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
                onClickNewTask = {},
                onItemFinish = {},
                onItemFavorite = {},
                onClickTaskItem = {}
            )
        }
    }
}

@Composable
fun TaskListBody(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),
    taskList: List<TaskItem> = emptyList(),
    onClickNewTask: () -> Unit = {},
    onItemFinish: (taskItem: TaskItem) -> Unit = {},
    onItemFavorite: (taskItem: TaskItem) -> Unit = {},
    onClickTaskItem: (Long) -> Unit,
    onClearListClick: () -> Unit = {}

) {

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Column(modifier = Modifier.padding(10.dp)) {
                    TaskBodyHeader(
                        onClickNewTask = onClickNewTask,
                        onClearListClick = onClearListClick
                    )
                    taskList.forEach { taskItem ->
                        TaskBodyItem(
                            taskItem,
                            onItemFinish = onItemFinish,
                            onItemFavorite = onItemFavorite,
                            onClickTaskItem = onClickTaskItem
                        )
                    }
                }

            }
        }

    }
}


@Composable
fun TaskBodyHeader(
    modifier: Modifier = Modifier,
    onClickNewTask: () -> Unit,
    onClearListClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onClickNewTask()
            }) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    "add task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                modifier = Modifier.clickable { onClickNewTask() },
                text = "Add New Task", style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            )
        }

        IconButton(onClick = { onClearListClick() }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Clear List",
                tint = MaterialTheme.colorScheme.primary
            )

        }
    }
}

@Composable
@Preview
fun TaskBodyItemPreview() {
    Column {
        TaskBodyItem(
            taskItem = TaskItem(
                1,
                "dasdsadsadsad sadsadsadsad asdsa" +
                        " dsad sadasd sad sad sad sadasdasdadsad as" +
                        " dasdsadsadsadasd asd sad asd asd sad asd"
            ), onClickTaskItem = {}
        )
        TaskBodyItem(
            taskItem = TaskItem(
                2,
                "dasdsadsadsad sadsadsadsad asdsa"
            ), onClickTaskItem = {}
        )
    }

}

@Composable
fun TaskBodyItem(
    taskItem: TaskItem,
    onItemFinish: (taskItem: TaskItem) -> Unit = {},
    onItemFavorite: (taskItem: TaskItem) -> Unit = {},
    onClickTaskItem: (Long) -> Unit
) {
    if (taskItem.isFinished) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { onItemFinish(taskItem.apply { isFinished = false }) }) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    "add task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .clickable {
                        onClickTaskItem(taskItem.id)
                    }
                    .weight(1f),
                text = taskItem.description ?: "",
                style = TextStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            )

            IconButton(onClick = { onItemFinish(taskItem.apply { isFavorite = !isFavorite }) }) {
                Icon(
                    imageVector = if (taskItem.isFavorite) Icons.Filled.Star else Icons.Outlined.Grade,
                    "add favorite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { onItemFinish(taskItem.apply { isFinished = true }) }) {
                Icon(
                    imageVector = Icons.Filled.RadioButtonUnchecked,
                    "add task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                modifier = Modifier
                    .clickable {
                        onClickTaskItem(taskItem.id)
                    }
                    .weight(1f),
                text = taskItem.description ?: " ", style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                ),
                overflow = TextOverflow.Ellipsis
            )


            IconButton(onClick = { onItemFinish(taskItem.apply { isFavorite = !isFavorite }) }) {
                Icon(
                    imageVector = if (taskItem.isFavorite) Icons.Filled.Star else Icons.Outlined.Grade,
                    "add favorite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

