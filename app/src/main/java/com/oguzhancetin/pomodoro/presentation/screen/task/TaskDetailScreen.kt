package com.oguzhancetin.pomodoro.presentation.screen.task

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.presentation.common.DetailTopBar
import com.oguzhancetin.pomodoro.presentation.theme.PomodoroTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    when (val state = uiState) {
        is TaskDetailUIState.Success -> {

            Scaffold(
                topBar = {
                    TaskDetailTopBar(
                        currentRoute = if (state.isUpdate) "Task Update" else "New Task",
                        navigateUp = { onBack() }
                    )
                },
                bottomBar = {
                    Column(Modifier.imePadding()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(modifier = Modifier.fillMaxWidth(fraction = 0.8f), onClick = {
                                viewModel.saveTask()
                                onBack()
                            }, enabled = state.isSaveButtonActive) {
                                Text("Save")
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (state.isUpdate.not())
                                Text(
                                    text = "Delete Task",
                                    color = Color.Red,
                                    modifier = Modifier.clickable {
                                        viewModel.deleteTask()
                                        onBack()
                                    }
                                )


                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                }
            ) { innerPadding ->
                TaskDetailScreenContent(
                    modifier = modifier
                        .padding(innerPadding),
                    onTextChange = { text -> viewModel.onTextChange(text) },
                    text = state.text,
                    category = state.selectedCategory
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
                Column() {
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
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Delete Category",
                            color = Color.Red,
                            modifier = Modifier.clickable {
                                /*TODO*/
                            }
                        )

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
    onTextChange: (String) -> Unit = { },
    text: String = "",
    category: Category?
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
                        .fillMaxHeight(0.5f),
                    text = text,
                    onTextChange = onTextChange,
                    category = category
                )
            }
        }
    }
}

@Composable
fun TextBodyPreview() {
    val category = Category(
        id = UUID.randomUUID(),
        name = "Work",
    )
    TextBody(
        Modifier
            .fillMaxWidth()
            .height(250.dp), category = category
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBody(
    modifier: Modifier = Modifier,
    category: Category?,
    onTextChange: (String) -> Unit = { },
    text: String = "",
) {

    val currentDateTime = Calendar.getInstance().time
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val formattedDate = formatter.format(currentDateTime)

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (text.isEmpty())
            focusRequester.requestFocus()
    }

    Column {
        Row(modifier = modifier) {
            TextField(
                modifier = Modifier
                    .fillMaxHeight()
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),
                value = text,
                onValueChange = { onTextChange(it) },
                placeholder = {
                    Text(
                        text = if (text.isNotEmpty()) "Task Name" else "",
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

        //Divider(thickness = 2.dp)
        Spacer(Modifier.height(12.dp))
        /*      Column(Modifier.padding(horizontal = 10.dp)) {
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

              }*/

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

@ExperimentalMaterial3Api
@Composable
fun TaskDetailTopBar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    currentRoute: String,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        title = { Text(text = currentRoute, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

        },
        modifier = modifier
    )
}