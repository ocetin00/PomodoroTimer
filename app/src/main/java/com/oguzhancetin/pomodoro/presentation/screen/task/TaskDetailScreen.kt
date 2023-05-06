package com.oguzhancetin.pomodoro.presentation.screen.task

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.DetailTopBar
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is TaskDetailUIState.Success -> {
            Scaffold(
                topBar = {
                    DetailTopBar(
                        currentRoute = state.taskItem?.let { "Task Update" } ?: "New Task",
                        canNavigateBack = true,
                        navigateUp = { onBack() }
                    )
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(modifier = Modifier.fillMaxWidth(fraction = 0.8f), onClick = {
                            viewModel.saveTask()
                            onBack()
                        }, enabled = (uiState as TaskDetailUIState.Success).isSaveButtonActive) {
                            Text("Save")
                        }
                    }

                }
            ) {

                TaskDetailScreenContent(
                    modifier = modifier
                        .padding(it),
                    state.categoryList,
                    state.selectedCategoryId,
                    onTextChange = { viewModel.onTextChange(it) },
                    text = state.text,
                    task = state.taskItem
                )

            }

        }

        is TaskDetailUIState.Loading -> {

        }

        is TaskDetailUIState.Error -> {

        }

    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun TaskDetailScreenContentPreview() {
    PomodoroTheme {
        Scaffold(
            topBar = {
                DetailTopBar(
                    currentRoute = "New Task", canNavigateBack = true, navigateUp = { }
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

            Text("Hello")

        }
    }

}

@Composable
fun TaskDetailScreenContent(
    modifier: Modifier = Modifier,
    categories: List<Category> = listOf(),
    selectedCategoryId: UUID? = null,
    onTextChange: (String) -> Unit = { },
    text: String = "",
    task: TaskItem? = null
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
                        .height(250.dp),
                    categories,
                    selectedCategoryId,
                    text = text,
                    onTextChange = onTextChange,
                    task = task
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
fun TextBody(
    modifier: Modifier = Modifier,
    categories: List<Category> = listOf(),
    selectedCategoryId: UUID? = null,
    onTextChange: (String) -> Unit = { },
    text: String = "",
    task: TaskItem? = null
) {


    val currentDateTime = Calendar.getInstance().time
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val formattedDate = formatter.format(currentDateTime)

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        task?.let {
            it.description?.let { it1 -> onTextChange(it1) }
        } ?: run {
            focusRequester.requestFocus()
        }
    }

    Column {
        Row(modifier = modifier) {
            TextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),
                value = text,
                onValueChange = { onTextChange(it) },
                placeholder = {
                    Text(
                        text = if (task == null) "Task Name" else "",
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
        Spacer(Modifier.height(12.dp))
        Column(Modifier.padding(horizontal = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "created date",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = formattedDate,
                    onValueChange = {},
                    readOnly = true,
                    // trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .width(200.dp)
                        .background(color = MaterialTheme.colorScheme.surface),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Category,
                    contentDescription = "created date",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                // SelectCategoryDropDown(categories, selectedCategoryId)
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SelectCategoryDropDown(
    categories: List<Category> = listOf(),
    selectedCategoryId: UUID? = null
) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var selectedCategoryName by remember {
        mutableStateOf(
            categories.find {
                it.id == selectedCategoryId
            }?.name ?: categories[0].name
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedCategoryName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .width(200.dp)
                    .padding(start = 0.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
            )



            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            selectedCategoryName = item.name
                            expanded = false
                            Toast.makeText(context, item.name, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}