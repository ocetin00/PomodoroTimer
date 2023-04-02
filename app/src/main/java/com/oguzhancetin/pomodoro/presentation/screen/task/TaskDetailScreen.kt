package com.oguzhancetin.pomodoro.presentation.screen.task

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.taskUIState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            MainAppBar(
                currentRoute = "TaskDetail", canNavigateBack = true, navigateUp = onBack
            )
        },
        bottomBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(modifier = Modifier.fillMaxWidth(fraction = 0.4f), onClick = { /*TODO*/ }) {
                    Text("Save")
                }
            }

        }
    ) {

        TaskDetailScreenContent(
            modifier = modifier
                .padding(it)

        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun TaskDetailScreenContentPreview() {
    PomodoroTheme {
        Scaffold(
            topBar = {
                MainAppBar(
                    currentRoute = "TaskDetail", canNavigateBack = true, navigateUp = { }
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(fraction = 0.4f),
                        onClick = { /*TODO*/ }) {
                        Text("Save")
                    }
                }
            }
        ) {

            TaskDetailScreenContent(
                modifier = Modifier
                    .padding(it)

            )


        }
    }

}

@Preview
@Composable
fun TaskDetailScreenContent(
    modifier: Modifier = Modifier
) {

    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 5.dp)
        ) {
            item {
                TextBody(
                    Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

        }
    }
}

@Composable
fun TextBodyPreview() {
    TextBody(
        Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBody(modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    var text by remember { mutableStateOf("") }

    Column {
        Row(modifier = modifier) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = "What are you planing?",
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                ),
            )
        }
        Divider(thickness = 2.dp)
        Spacer(Modifier.height(20.dp))
        Column(Modifier.padding(horizontal = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "created date",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("12/23/2023", fontSize = MaterialTheme.typography.headlineSmall.fontSize)
            }
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Category,
                    contentDescription = "created date",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("Default Category", fontSize = MaterialTheme.typography.headlineSmall.fontSize)
            }

        }

    }


}