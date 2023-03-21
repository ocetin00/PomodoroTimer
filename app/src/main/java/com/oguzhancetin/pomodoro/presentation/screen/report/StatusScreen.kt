package com.oguzhancetin.pomodoro.presentation.screen.report


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.PomodoroAppChart
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.convertListToXYPairs
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter

import java.util.*
import kotlin.math.roundToInt

@Composable
fun StatusScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: ReportViewModal = hiltViewModel()
) {

    val reportUIState by viewModel.reportUIState.collectAsState()

    reportUIState.errorMessage?.let {

    }

    if (reportUIState.isLoading) {

    } else {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        DisposableEffect(systemUiController, useDarkIcons) {
            // Update all of the system bar colors to be transparent, and use
            // dark icons if we're in light theme
            systemUiController.setSystemBarsColor(
                color = androidx.compose.ui.graphics.Color.Transparent,
                darkIcons = useDarkIcons
            )

            // setStatusBarColor() and setNavigationBarColor() also exist

            onDispose {}
        }
        StatusScreenContent(modifier, reportUIState.pomodoroList, reportUIState.taskList,onBack)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreenContent(
    modifier: Modifier = Modifier,
    pomodoroList: List<Pomodoro> = listOf(),
    taskList: List<TaskItem> = listOf(),
    onBack: () -> Unit = {},
) {

    val pomodoroChartPair = convertListToXYPairs(pomodoroList)
    val taskListChartPair = convertListToXYPairs(taskList)

    PomodoroTheme {
        Scaffold(
            topBar = {
                MainAppBar(
                    currentRoute = "Status",
                    canNavigateBack = true,
                    navigateUp = {onBack()}
                )
            }
        ) { innerPadding ->

            Surface(
                modifier = Modifier,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Spacer(Modifier.height(30.dp))
                    PomodoroChart(
                        modifier = Modifier
                            .height(270.dp)
                            .padding(horizontal = 15.dp),
                        chartPair = pomodoroChartPair
                    )
                    Spacer(Modifier.height(45.dp))
                    TaskChart(
                        modifier = Modifier
                            .height(270.dp)
                            .padding(horizontal = 15.dp),
                        chartPair = taskListChartPair
                    )

                }
            }
        }

    }


}


@Preview
@Composable
fun PomodoroChart(
    modifier: Modifier = Modifier,
    chartPair: List<Pair<Int, Int>> = listOf(
        Pair(0, 1),
        Pair(1, 3),
        Pair(2, 3),
        Pair(3, 2),
        Pair(4, 5),
        Pair(5, 1),
        Pair(6, 1)
    )
) {

    val daysOfWeek = listOf("Mo", "Tu", "Wd", "Th", "Fr", "St", "Sn")
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> daysOfWeek[x.toInt() % daysOfWeek.size] }


    val y = listOf("0", "1", "2", "3", "4", "5", "6")
    val startAxisValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { x, chartValues ->
       // val entries = chartValues.chartEntryModel.entries as? List<Pomodoro>
        if(x.toInt() == 0){
            "0"
        }else{
            (x.roundToInt()+2).toString()
        }

    }


    PomodoroTheme {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = modifier.padding(vertical = 2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ChartHeader("Pomodoro Activity", onFilterAction = {})
                Spacer(Modifier.height(10.dp))
                PomodoroAppChart(chartPair, bottomAxisValueFormatter)
            }
        }

    }
}

@Composable
fun TaskChart(modifier: Modifier = Modifier, chartPair: List<Pair<Int, Int>> = listOf()) {

    val daysOfWeek = listOf("Mo", "Tu", "Wd", "Th", "Fr", "St", "Sn")
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> daysOfWeek[x.toInt() % daysOfWeek.size] }

    val y = listOf("0", "1", "2", "3", "4", "5", "6")
    val startAxisValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { x, _ ->
        y[x.toInt() % y.size]
    }

    PomodoroTheme {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = modifier.padding(vertical = 2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ChartHeader("Task Activity", onFilterAction = {})
                Spacer(Modifier.height(10.dp))
                PomodoroAppChart(chartPair, bottomAxisValueFormatter)
            }
        }

    }
}

@Composable
fun ChartHeader(title: String, onFilterAction: (ChartFilterStatus) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            // modifier = Modifier.border(border = BorderStroke(1.dp,Color.DarkGray))
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onBackground,
                imageVector = Icons.Filled.AddChart,
                contentDescription = "Options"
            )
            Spacer(Modifier.width(2.dp))
            Text(
                title,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
            // modifier = Modifier.border(border = BorderStroke(1.dp,Color.DarkGray))
        ) {
            Text(
                "This week",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
            Icon(
                tint = MaterialTheme.colorScheme.onBackground,
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Options"
            )
        }

    }
}

enum class ChartFilterStatus {
    WEEK, MONTH
}



