package com.oguzhancetin.pomodoro.ui

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.oguzhancetin.pomodoro.presentation.screen.setting.SettingViewModel
import com.oguzhancetin.pomodoro.core.time.WorkUtil.getMinute
import com.oguzhancetin.pomodoro.presentation.theme.PomodoroTheme
import com.oguzhancetin.pomodoro.core.time.Time
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun SettingScreenPreview() {
    PomodoroTheme {
        Scaffold(
            topBar = {
                SettingTopBar(
                    currentRoute = "Setting", navigateUp = { }
                )
            }
        ) { innerPadding ->

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer

            ) {

                SettingTopBar(
                    currentRoute = "Setting", navigateUp = { }
                )
                SettingScreenContent(
                    modifier = Modifier.padding(innerPadding),
                    intervalSettingParameters = IntervalSettingParameters(
                        pomodoroTime = 0L,
                        longTime = 0L,
                        shortTime = 0L,
                        onIncrease = {
                        },
                        onDecrease = {
                        }
                    ),
                    GeneralSettingsParameters(
                        toggleTheme = { },
                        themeToogleState = false,
                        tickSoundToggleState = true,
                        toggleThickSound = { },
                    )

                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: SettingViewModel = hiltViewModel()
) {
    val pomodoroTime by viewModel.pomodoroTime.collectAsState(initial = 0L)
    val longTime by viewModel.longTime.collectAsState(initial = 0L)
    val shortTime by viewModel.shortTime.collectAsState(initial = 0L)
    Log.e("PomdoroTime", pomodoroTime.toString())


    Scaffold(
        topBar = {
            SettingTopBar(
                currentRoute = "Setting",
                navigateUp = onBack
            )
        }
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer

        ) {
            SettingScreenContent(
                modifier = modifier.padding(innerPadding),
                intervalSettingParameters = IntervalSettingParameters(
                    pomodoroTime = pomodoroTime,
                    longTime = longTime,
                    shortTime = shortTime,
                    onIncrease = { time ->
                        viewModel.increaseTime(time)
                    },
                    onDecrease = { time ->
                        viewModel.decreaseTime(time)
                    }
                ),
                GeneralSettingsParameters(
                    toggleTheme = { viewModel.ToggleAppTheme() },
                    themeToogleState = viewModel.isDarkTheme.collectAsState(initial = false).value,
                    tickSoundToggleState = viewModel.isSilentNotification.collectAsState(
                        initial = false
                    ).value,
                    toggleThickSound = { viewModel.toggleTickSound() },
                )
            )
        }
    }


}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun SettingScreenContent(
    modifier: Modifier = Modifier,
    intervalSettingParameters: IntervalSettingParameters,
    generalSettingsParameters: GeneralSettingsParameters
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        TabRow(
            modifier = Modifier.padding(paddingValues = PaddingValues(horizontal = 20.dp)),
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .wrapContentWidth()
                        .pagerTabIndicatorOffset(pagerState, tabPositions)
                        .padding(horizontal = 50.dp)
                )
            }
        ) {
            Tab(
                modifier = Modifier.wrapContentWidth(),
                text = { Text("General Setting") },
                selected = pagerState.currentPage == 0,
                onClick = {
                    scope.launch { pagerState.scrollToPage(0) }
                },
            )
            Tab(
                modifier = Modifier.wrapContentWidth(),
                text = { Text("Interval Setting") },
                selected = pagerState.currentPage == 1,
                onClick = {
                    scope.launch { pagerState.scrollToPage(1) }
                },
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        HorizontalPager(
            userScrollEnabled = false,
            count = 2,
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> GeneralSetting(
                    generalSettingsParameters = generalSettingsParameters
                )

                1 -> IntervalSetting(
                    parameters = intervalSettingParameters
                )

                else -> GeneralSetting(
                    generalSettingsParameters = generalSettingsParameters
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun GeneralSettingsRowPreview() {
    PomodoroTheme {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IntervalSettingRow(
                    title = "Short Time",
                    onDecrease = { },
                    onIncrease = { },
                    time = "12"
                )
            }

        }
    }


}

@Composable
fun GeneralSettingRow(title: String, checkedState: Boolean, onChecked: (Boolean) -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Card(
            modifier = Modifier
                .height(55.dp)
                .background(color = MaterialTheme.colorScheme.onPrimary),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        ) {
            Row(
                modifier = Modifier
                    .height(55.dp)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp))
                    .fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = title, color = MaterialTheme.colorScheme.onPrimaryContainer)

                Switch(
                    checked = checkedState,
                    onCheckedChange = { onChecked(it) })
            }
        }


    }
}

@Composable
fun IntervalSettingRow(
    title: String, onDecrease: (Time) -> Unit, onIncrease: (Time) -> Unit,
    time: String
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Card(
            modifier = Modifier
                .height(55.dp)
                .background(color = MaterialTheme.colorScheme.onPrimary),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        ) {
            Row(
                modifier = Modifier
                    .height(55.dp)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp))
                    .fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = title)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onDecrease(Time.Pomodoro()) }) {
                        Icon(imageVector = Icons.Filled.Remove, "decrease")
                    }

                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier
                            .width(40.dp)
                            .clip(shape = MaterialTheme.shapes.extraLarge)
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                    ) {
                        Text(
                            time,
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
                        )
                    }

                    IconButton(onClick = { onIncrease(Time.Pomodoro()) }) {
                        Icon(imageVector = Icons.Filled.Add, "increase")
                    }
                }
            }
        }
    }
}

@Composable
fun GeneralSetting(
    generalSettingsParameters: GeneralSettingsParameters,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GeneralSettingRow(
            title = "Dark Theme",
            checkedState = generalSettingsParameters.themeToogleState,
            onChecked = { generalSettingsParameters.toggleTheme() }
        )
        Spacer(modifier = Modifier.height(12.dp))
        GeneralSettingRow(
            title = "Disable Sounds",
            checkedState = generalSettingsParameters.tickSoundToggleState,
            onChecked = { generalSettingsParameters.toggleThickSound() }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun IntervalSetting(
    parameters: IntervalSettingParameters
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IntervalSettingRow(
            title = "Pomodoro Time",
            onDecrease = { parameters.onDecrease(Time.Pomodoro()) },
            onIncrease = { parameters.onIncrease(Time.Pomodoro()) },
            time = parameters.pomodoroTime.getMinute().toString()
        )
        Spacer(modifier = Modifier.height(12.dp))
        IntervalSettingRow(
            title = "Short Time",
            onDecrease = { parameters.onDecrease(Time.Short()) },
            onIncrease = { parameters.onIncrease(Time.Short()) },
            time = parameters.shortTime.getMinute().toString()
        )
        Spacer(modifier = Modifier.height(12.dp))
        IntervalSettingRow(
            title = "Long Time",
            onDecrease = { parameters.onDecrease(Time.Long()) },
            onIncrease = { parameters.onIncrease(Time.Long()) },
            time = parameters.longTime.getMinute().toString()
        )
        Spacer(modifier = Modifier.height(12.dp))

    }
}

@ExperimentalMaterial3Api
@Composable
fun SettingTopBar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    currentRoute: String,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        ),
        title = { Text(text = currentRoute, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = "Back"
                )
            }

        },
        modifier = modifier
    )
}

data class IntervalSettingParameters(
    val pomodoroTime: Long = 0L,
    val shortTime: Long = 0L,
    val longTime: Long = 0L,
    val onIncrease: (Time) -> Unit = {},
    val onDecrease: (Time) -> Unit = {},
)

data class GeneralSettingsParameters(
    val toggleTheme: () -> Unit = {},
    val toggleThickSound: () -> Unit = {},
    val themeToogleState: Boolean,
    val tickSoundToggleState: Boolean,
)

