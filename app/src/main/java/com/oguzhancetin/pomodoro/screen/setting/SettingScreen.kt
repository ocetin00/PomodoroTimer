package com.oguzhancetin.pomodoro.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.screen.setting.SettingViewModel
import com.oguzhancetin.pomodoro.ui.theme.light_onSurfaceRed
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.util.getMinute
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {},
    viewModel: SettingViewModel = hiltViewModel()
) {
    val pomodoroTime by viewModel.pomodoroTime.collectAsState(initial = 0L)
    val longTime by viewModel.longTime.collectAsState(initial = 0L)
    val shortTime by viewModel.shortTime.collectAsState(initial = 0L)
    Log.e("PomdoroTime",pomodoroTime.toString())
    Scaffold(
        topBar = {
            SettingAppBar(openDrawer = openDrawer)
        },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)

        SettingScreenContent(
            modifier = contentModifier,
            intervalSettingParameters = IntervalSettingParameters(
                pomodoroTime = pomodoroTime,
                longTime = longTime,
                shortTime = shortTime,
                onIncrease = { time ->
                    viewModel.increaseTime(time)
                },
                onDecrease = {time ->
                    viewModel.decreaseTime(time)
                }
            )
        )


    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SettingScreenContent(
    modifier: Modifier = Modifier,
    intervalSettingParameters: IntervalSettingParameters = IntervalSettingParameters()
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = modifier) {
            TabRow(
                modifier = Modifier.padding(paddingValues = PaddingValues(horizontal = 20.dp)),
                backgroundColor = MaterialTheme.colorScheme.background,
                selectedTabIndex = pagerState.currentPage,
                divider = {},
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
                    0 -> GeneralSetting()
                    1 -> IntervalSetting(
                        parameters = intervalSettingParameters
                    )
                    else -> GeneralSetting()
                }
            }
        }
    }

}

@Preview
@Composable
fun GeneralSetting() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            contentColor = MaterialTheme.colorScheme.inverseSurface
        ) {
            Row(
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(0.8f)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Dark Theme")
                var checkedState by remember { mutableStateOf<Boolean>(false) }
                Switch(checked = checkedState, onCheckedChange = { checkedState = !checkedState })
            }

        }
        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            contentColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(0.8f)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Daily Notification")
                var checkedState by remember { mutableStateOf<Boolean>(false) }
                Switch(checked = checkedState, onCheckedChange = { checkedState = !checkedState })
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            contentColor = MaterialTheme.colorScheme.inverseSurface
        ) {
            Row(
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(0.8f)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Dark Theme")
                var checkedState by remember { mutableStateOf<Boolean>(false) }
                Switch(checked = checkedState, onCheckedChange = { checkedState = !checkedState })
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            contentColor = MaterialTheme.colorScheme.inverseSurface
        ) {
            Row(
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(0.8f)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Enable Tick Sound")
                var checkedState by remember { mutableStateOf<Boolean>(false) }
                Switch(checked = checkedState, onCheckedChange = { checkedState = !checkedState })
            }

        }


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


        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            contentColor = MaterialTheme.colorScheme.inverseSurface
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(55.dp)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = "Pomodoro Time")
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { parameters.onDecrease(Times.Pomodoro) }) {
                        Icon(imageVector = Icons.Filled.Remove, "decrease")
                    }

                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier
                            .width(40.dp)
                            .clip(shape = MaterialTheme.shapes.extraLarge)
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                    ) {
                        Text(
                            parameters.pomodoroTime.getMinute().toString(),
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
                        )
                    }

                    IconButton(onClick = {parameters.onIncrease(Times.Pomodoro) }) {
                        Icon(imageVector = Icons.Filled.Add, "increase")
                    }

                }


            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            contentColor = MaterialTheme.colorScheme.inverseSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(55.dp)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = "Short Time")
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { parameters.onDecrease(Times.Short) }) {
                        Icon(imageVector = Icons.Filled.Remove, "decrease")
                    }


                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier
                            .width(40.dp)
                            .clip(shape = MaterialTheme.shapes.extraLarge)
                            .background(MaterialTheme.colorScheme.inverseOnSurface)

                    ) {
                        Text(
                            parameters.shortTime.getMinute().toString(),
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
                        )
                    }

                    IconButton(onClick = {parameters.onIncrease(Times.Short) }) {
                        Icon(imageVector = Icons.Filled.Add, "increase")
                    }

                }


            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            contentColor = MaterialTheme.colorScheme.inverseSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(55.dp)
                    .padding(paddingValues = PaddingValues(horizontal = 10.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = "Long Time")
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { parameters.onDecrease(Times.Long) }) {
                        Icon(imageVector = Icons.Filled.Remove, "decrease")
                    }
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier
                            .width(40.dp)
                            .clip(shape = MaterialTheme.shapes.extraLarge)
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                    ) {
                        Text(
                            parameters.longTime.getMinute().toString(),
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
                        )
                    }

                    IconButton(onClick = {parameters.onIncrease(Times.Long) }) {
                        Icon(imageVector = Icons.Filled.Add, "increase")
                    }

                }


            }

        }
        Spacer(modifier = Modifier.height(12.dp))

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingAppBar(
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

data class IntervalSettingParameters(
    val pomodoroTime: Long = 0L,
    val shortTime: Long = 0L,
    val longTime: Long = 0L,
    val onIncrease: (Times) -> Unit = {},
    val onDecrease: (Times) -> Unit = {},

    )