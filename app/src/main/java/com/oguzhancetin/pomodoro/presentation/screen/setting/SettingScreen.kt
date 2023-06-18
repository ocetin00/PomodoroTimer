package com.oguzhancetin.pomodoro.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.oguzhancetin.pomodoro.presentation.screen.setting.SettingViewModel
import com.oguzhancetin.pomodoro.common.util.Time.WorkUtil.getMinute
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme
import com.oguzhancetin.pomodoro.util.Times
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
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
                currentRoute = "Setting", navigateUp = onBack
            )

        }
    ) { innerPadding ->
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
                tickSoundToggleState = viewModel.isSilentNotification.collectAsState(initial = false).value,
                toggleThickSound = { viewModel.toggleTickSound() },
            )
        )
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
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer

    ) {
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
    title: String, onDecrease: (Times) -> Unit, onIncrease: (Times) -> Unit,
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
                    IconButton(onClick = { onDecrease(Times.Pomodoro()) }) {
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

                    IconButton(onClick = { onIncrease(Times.Pomodoro()) }) {
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
            title = "Enable Tick Sound",
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
            onDecrease = { parameters.onDecrease(Times.Pomodoro()) },
            onIncrease = { parameters.onIncrease(Times.Pomodoro()) },
            time = parameters.pomodoroTime.getMinute().toString()
        )
        Spacer(modifier = Modifier.height(12.dp))
        IntervalSettingRow(
            title = "Short Time",
            onDecrease = { parameters.onDecrease(Times.Short()) },
            onIncrease = { parameters.onIncrease(Times.Short()) },
            time = parameters.shortTime.getMinute().toString()
        )
        Spacer(modifier = Modifier.height(12.dp))
        IntervalSettingRow(
            title = "Long Time",
            onDecrease = { parameters.onDecrease(Times.Long()) },
            onIncrease = { parameters.onIncrease(Times.Long()) },
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
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
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
    val onIncrease: (Times) -> Unit = {},
    val onDecrease: (Times) -> Unit = {},
)

data class GeneralSettingsParameters(
    val toggleTheme: () -> Unit = {},
    val toggleThickSound: () -> Unit = {},
    val themeToogleState: Boolean,
    val tickSoundToggleState: Boolean,
)

