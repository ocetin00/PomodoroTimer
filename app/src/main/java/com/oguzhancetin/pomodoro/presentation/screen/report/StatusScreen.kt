package com.oguzhancetin.pomodoro.presentation.screen.report


import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.DashPathEffect
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ColorInt
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.MainAppBar
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.PomodoroAppChart
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.CustomBarChartRender
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.convertListToXYPairs
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.getThemeColor
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
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
        StatusScreenContent(modifier, reportUIState.pomodoroList, reportUIState.taskList, onBack)

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


    Scaffold(
        topBar = {
            MainAppBar(
                currentRoute = "Status",
                canNavigateBack = true,
                navigateUp = { onBack() }
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
                        .padding(horizontal = 5.dp),
                    chartPair = taskListChartPair
                )

            }
        }
    }


}

@Composable
@Preview
fun TaskChartPreview() {
    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        PomodoroChart(
            modifier = Modifier
                .height(270.dp)

        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PomodoroChart(
    modifier: Modifier = Modifier,
    chartPair: List<Pair<Int, Int>> = listOf(
        Pair(0, 1),
        Pair(1, 3),
        Pair(2, 3),
        Pair(3, 10),
        Pair(4, 5),
        Pair(5, 1),
        Pair(6, 1)
    )
) {


    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
    ) {
        Column(
            modifier = modifier.padding(vertical = 2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(5.dp))
            ChartHeader("Pomodoro Activity", onFilterAction = {})
            Spacer(Modifier.height(10.dp))
            CustomView(chartPair, isSystemInDarkTheme())
        }
    }


}

@Composable
fun TaskChart(modifier: Modifier = Modifier, chartPair: List<Pair<Int, Int>> = listOf()) {

    val daysOfWeek = listOf("Mo", "Tu", "Wd", "Th", "Fr", "St", "Sn")
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> daysOfWeek[x.toInt() % daysOfWeek.size] }

    val y = listOf("0", "1", "2", "3", "4", "5", "6")
    val startAxisValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { x, y ->
        Log.d("x,y", "$x , $y")
        "${x + 2}"
    }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
    ) {
        Column(
            modifier = modifier.padding(vertical = 2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChartHeader("Task Activity", onFilterAction = {})
            Spacer(Modifier.height(10.dp))
            PomodoroAppChart(chartPair, bottomAxisValueFormatter, startAxisValueFormatter)
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
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                imageVector = Icons.Filled.AddChart,
                contentDescription = "Options"
            )
            Spacer(Modifier.width(2.dp))
            Text(
                title,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        /* Row(
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
         }*/

    }
}

enum class ChartFilterStatus {
    WEEK, MONTH
}


@Composable
fun CustomView(
    chartPair: List<Pair<Int, Int>> = listOf(
        Pair(0, 1),
        Pair(1, 3),
        Pair(2, 3),
        Pair(3, 2),
        Pair(4, 10),
        Pair(5, 1),
        Pair(6, 1)
    ),
    isDarkMode: Boolean = false
) {
    val labelColor =
        if (isDarkMode) Color.parseColor("#FFD0E4FF") else Color.parseColor("#FF001D34")
    val barColor = if (isDarkMode) Color.parseColor("#0062A0") else Color.parseColor("#0062A0")


    val maxY = chartPair.maxBy { it.second }.second

    val entries = chartPair.mapIndexed { index, pair ->
        BarEntry(index.toFloat(), pair.second.toFloat())
    }

    val dataSet = BarDataSet(entries, "").apply {
        setDrawValues(false)
        setDrawIcons(false)
        color = barColor
    }

    val barData = BarData(dataSet).apply {
        this.barWidth = 0.8f
    }
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            // Creates view
            BarChart(
                context
            )//.also { it.animateY(500) }
        },
        update = { view ->
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication
            view.data = barData
            view.xAxis.apply {
                valueFormatter = MyXAxisFormatter()
                position = XAxis.XAxisPosition.BOTTOM
                axisMinimum = 0f
                textSize = 10f
                textColor = labelColor
                setDrawAxisLine(false)
                setDrawGridLines(false)
                granularity = 1f
                axisMinimum = -0.5f

            }
            view.axisLeft.apply {
                setGridDashedLine(DashPathEffect(floatArrayOf(15f, 5f), 1f))
                setDrawAxisLine(false)
                axisMaximum = (kotlin.math.ceil(maxY.toDouble() / 10) * 10).toFloat()
                this.axisLineColor = labelColor
                this.textColor = labelColor
            }
            view.setTouchEnabled(false)
            view.axisRight.isEnabled = false
            view.description = Description().also { it.text = "" }
            view.legend.form = Legend.LegendForm.NONE
            view.renderer =
                CustomBarChartRender(view, view.animator, view.viewPortHandler).also {
                    it.setRadius(
                        50
                    )
                }
            view.invalidate()

        }
    )
}


@Preview()
@Composable
fun Chart() {
    Column(Modifier.fillMaxSize()) {
        Text("Look at this CustomView!")
        CustomView()
    }

}

class MyXAxisFormatter : ValueFormatter() {
    private val days = arrayOf("Mo", "Tu", "Wed", "Th", "Fr", "Sa", "Su")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}