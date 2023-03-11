package com.oguzhancetin.pomodoro.presentation.screen.report

import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.presentation.ui.theme.light_onRedBackground
import com.oguzhancetin.pomodoro.common.util.chart.XAxisValueFormatter
import com.oguzhancetin.pomodoro.common.util.removeDetails
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.presentation.ui.theme.PomodoroTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

import java.util.*

@Composable
fun StatusScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit ={} ,
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
        StatusScreenContent(modifier,reportUIState.pomodoroList)

    }


}

@Composable
fun PomodoroChart(modifier: Modifier = Modifier,pomodoroList: List<Pomodoro> = listOf()) {

    val entries = convertEntryList(pomodoroList)
    /*
        val entr = listOf<Entry>(
            Entry(1f, 2f),
            Entry(2f, 3f),
            Entry(3f, 1f),
            Entry(4f, 3f),
            Entry(5f, 3f),
            Entry(6f, 3f),
            Entry(7f, 8f)
        )*/
    val lineDataSet = LineDataSet(entries, "task_items")


    lineDataSet.setDrawValues(false);
    lineDataSet.setDrawCircles(false);
    lineDataSet.setDrawFilled(true);
    lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER;
    lineDataSet.cubicIntensity = 0.2f;
    val lineData = LineData(lineDataSet)


    Card (
        colors  = CardDefaults.cardColors(),
        modifier = modifier
    ){
        Row(modifier = Modifier
            .fillMaxWidth()){
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),horizontalArrangement = Arrangement.SpaceBetween){
                    Text("Pomodoro Activty",color = androidx.compose.ui.graphics.Color.Black )
                    Row(){
                        Text("This week" ,color = androidx.compose.ui.graphics.Color.Black)
                        Icon(
                            tint = androidx.compose.ui.graphics.Color.Black,
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Options"
                        )
                    }

                }
                AndroidView(factory = { context ->
                    LineChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            // on below line we are specifying layout
                            // params as MATCH PARENT for height and width.
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        data = lineData
                        axisRight.isEnabled = false
                        xAxis.setDrawGridLines(false)
                        axisLeft.setDrawGridLines(false)
                        setTouchEnabled(false)
                        xAxis.axisMaximum = 6.5f
                        xAxis.granularity = 1f
                        axisLeft.axisMinimum = 0f
                        axisLeft.axisMaximum = 10f
                        xAxis.apply {
                            valueFormatter = XAxisValueFormatter()
                            position = XAxis.XAxisPosition.BOTTOM;
                            textSize = 10f;
                            textColor = Color.RED;
                            //.setValueFormatter(new MyCustomFormatter());
                        }
                    }
                }, modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp),
                    update = {
                        it.invalidate()
                    })

            }

        }

    }


}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreenContent(
    modifier: Modifier = Modifier,
    pomodoroList: List<Pomodoro> = listOf()
) {

    PomodoroTheme {
        Scaffold(
            topBar = {
                MainAppBar(
                    currentRoute = "Status")
            }
        ) { innerPadding ->

            Surface(modifier = Modifier,
                color = MaterialTheme.colorScheme.primaryContainer) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Spacer(modifier.height(45.dp))
                    PomodoroChart(
                        Modifier
                            .height(250.dp)
                            .padding(horizontal = 35.dp),pomodoroList)
                    Spacer(modifier.height(45.dp))
                    PomodoroChart(
                        Modifier
                            .height(250.dp)
                            .padding(horizontal = 35.dp),pomodoroList)

                }
            }
        }

    }




}

fun convertEntryList(sortedTaskItems: List<Pomodoro>): List<Entry> {

    val calendar = Calendar.getInstance(TimeZone.getDefault())
    val entries = mutableListOf<Entry>()
    val daysValue = mutableMapOf<Long, Int>()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    calendar.removeDetails()

    //add week days
    repeat(7) {
        daysValue.put(
            calendar.timeInMillis, 0
        )
        calendar.add(Calendar.DATE, 1)
    }


    //sorted to obtain which day and added to days hasMap
    sortedTaskItems.forEach { pomodoroItem ->
        daysValue.keys.forEach { key ->
            if (pomodoroItem.createdDate == key) {
                val currentValue = daysValue[key] ?: 0
                daysValue[key] = currentValue + 1
            }
        }
    }

    var tempCounter = 0
    daysValue.forEach {
        entries.add(Entry(tempCounter.toFloat(), it.value.toFloat()))
        tempCounter++
    }

    return entries


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusAppBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(id = R.string.app_name)
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = { Text(text = title, color = light_onRedBackground) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                    tint = light_onRedBackground
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun ChartPreview() {
    val chartEntryModel = entryModelOf(4f, 12f, 8f, 16f)
    Chart(
        chart = lineChart(),
        model = chartEntryModel,
        startAxis = startAxis(),
        bottomAxis = bottomAxis(),
    )
}
