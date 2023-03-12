package com.oguzhancetin.pomodoro.presentation.screen.report


import android.graphics.Color
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.PomodoroAppChart
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.convertPomodoroListToXYPairs
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.entryModelOf
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.rememberChartStyle
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.LineComponent

import java.util.*

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
        StatusScreenContent(modifier, reportUIState.pomodoroList)

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun StatusScreenContent(
    modifier: Modifier = Modifier,
    pomodoroList: List<Pomodoro> = listOf()
) {

    val pomodoroChartPair = convertPomodoroListToXYPairs(pomodoroList)

    PomodoroTheme {
        Scaffold(
            topBar = {
                MainAppBar(
                    currentRoute = "Status",
                    canNavigateBack = true,
                    navigateUp = {}
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
                        chartPair = pomodoroChartPair
                    )

                }
            }
        }

    }


}


@Composable
@Preview
fun PomodoroChart(modifier: Modifier = Modifier, chartPair: List<Pair<Int, Int>> = listOf()) {

    val daysOfWeek = listOf("Mo", "Tu", "Wd", "Th", "Fr", "St", "Sn")
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> daysOfWeek[x.toInt() % daysOfWeek.size] }

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



