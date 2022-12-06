package com.oguzhancetin.pomodoro.screen.task


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import com.oguzhancetin.pomodoro.util.removeDetails
import java.util.*

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val taskItems = viewModel.taskItems.collectAsState(initial = listOf())
            TaskScreenContent(
                modifier = modifier,
                taskItems = taskItems.value,
                onAddItem = { taskItem -> viewModel.addTask(taskItem) },
                onItemFinish = { taskItem -> viewModel.updateTask(taskItem) },
                onItemFavorite = { taskItem -> viewModel.updateTask(taskItem) }
            )
}


@Preview
@Composable
fun TaskScreenContent(
    modifier: Modifier = Modifier,
    taskItems: List<TaskItem> = listOf<TaskItem>(),
    onAddItem: (taskItem: TaskItem) -> Unit = {},
    onItemFinish: (taskItem: TaskItem) -> Unit = {},
    onItemFavorite: (taskItem: TaskItem) -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            TaskItemAdd(
                Modifier.padding(horizontal = 5.dp),
                onAddItem = { taskItem -> onAddItem(taskItem) })
        }

        items(taskItems) { item ->
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .padding(horizontal = 10.dp)
            )
            TaskItemContent(
                modifier = Modifier.padding(horizontal = 5.dp),
                taskItem = item,
                onItemFavorite = { taskItem -> onItemFavorite(taskItem) },
                onItemFinish = { taskItem -> onItemFinish(taskItem) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TaskItemAdd(modifier: Modifier = Modifier, onAddItem: (taskItem: TaskItem) -> Unit = {}) {
    Card(
        shape = MaterialTheme.shapes.medium,
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
                onAddItem(
                    TaskItem(
                        id = UUID.randomUUID(),
                        description = text.text,
                        createdDate = System.currentTimeMillis(),
                        isFavorite = false,
                        doneDate = null
                    )
                )
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
                modifier = textFieldModifier,
                value = text, onValueChange = {
                    if (it.text.length <= 23) text = it
                }, placeholder = {
                    Text(text = "Bir görev ekle")
                }, trailingIcon = {

                }, colors = TextFieldDefaults.outlinedTextFieldColors(
                    errorBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
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
    val calendar: Calendar = Calendar.getInstance()
    calendar.removeDetails()
    Card(
        shape = MaterialTheme.shapes.medium,
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