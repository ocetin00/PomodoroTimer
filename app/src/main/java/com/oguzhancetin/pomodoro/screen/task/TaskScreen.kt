package com.oguzhancetin.pomodoro.screen.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import com.oguzhancetin.pomodoro.ui.theme.PomodoroTheme
import com.oguzhancetin.pomodoro.ui.theme.light_onSurfaceRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    PomodoroTheme() {
        Scaffold(
            topBar = {
                TasKAppBar(openDrawer = onBack)
            },
            modifier = modifier
        ) { innerPadding ->

            val contentModifier = Modifier
                .padding(innerPadding)
            TaskScreenContent(
                modifier = contentModifier,
                taskItems = listOf(
                    TaskItem(1, "task1",1121),
                    TaskItem(2,"task2",32132)
                ))
        }
    }

}

@Preview
@Composable
fun TaskScreenContent(
    modifier: Modifier = Modifier,
    taskItems: List<TaskItem> = listOf<TaskItem>()
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(3.dp))
            TaskItemAdd(Modifier.padding(horizontal = 5.dp))
        }

        items(taskItems) { item ->
            Spacer(modifier = Modifier.height(3.dp))
            TaskItemContent(modifier = Modifier.padding(horizontal = 5.dp), taskItem = item)
        }
    }
}

@Preview
@Composable
fun TaskItemAdd(modifier: Modifier = Modifier) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(paddingValues = PaddingValues(horizontal = 20.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_task)
            )
            Text(text = "Bir görev ekle")
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = stringResource(R.string.add_task)
            )

        }
    }
}

@Preview
@Composable
fun TaskItemContent(
    taskItem: TaskItem = TaskItem(
        2,
        "deneme task", 12321321L
    ), modifier: Modifier = Modifier
) {

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(paddingValues = PaddingValues(horizontal = 20.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_task)
            )
            Text(text = taskItem.description)
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = stringResource(R.string.add_task)
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasKAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(id = R.string.app_name)
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = { Text(text = title, color = light_onSurfaceRed) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                    tint = light_onSurfaceRed
                )
            }
        },
        modifier = modifier
    )
}
