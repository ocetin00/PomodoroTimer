package com.oguzhancetin.pomodoro.presentation.screen.report


import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.graphics.DashPathEffect
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.core.model.Pomodoro
import com.oguzhancetin.pomodoro.core.model.TaskItem
import com.oguzhancetin.pomodoro.presentation.common.MainAppBar
import com.oguzhancetin.pomodoro.presentation.common.RoundedBarChart
import com.oguzhancetin.pomodoro.presentation.common.util.CustomBarChartRender
import com.oguzhancetin.pomodoro.presentation.common.util.convertListToXYPairs
import org.koin.androidx.compose.koinViewModel
import java.util.*


@Composable
fun StatusScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: ReportViewModal = koinViewModel()
) {

    val reportUIState by viewModel.reportUIState.collectAsState()

    reportUIState.errorMessage?.let {

    }

    if (reportUIState.isLoading) {

    } else {
        StatusScreenContent(
            modifier,
            reportUIState.pomodoroList,
            reportUIState.taskList,
            onBack,
            reportUIState.isDarkTheme ?: false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreenContent(
    modifier: Modifier = Modifier,
    pomodoroList: List<Pomodoro> = listOf(),
    taskList: List<TaskItem> = listOf(),
    onBack: () -> Unit = {},
    isDarkTheme: Boolean = false,
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
                    chartPair = pomodoroChartPair,
                    isDarkTheme = isDarkTheme
                )
                Spacer(Modifier.height(45.dp))
                TaskChart(
                    modifier = Modifier
                        .height(270.dp)
                        .padding(horizontal = 15.dp),
                    chartPair = taskListChartPair,
                    isDarkTheme = isDarkTheme
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
        Pair(0, 0),
        Pair(1, 0),
        Pair(2, 0),
        Pair(3, 0),
        Pair(4, 0),
        Pair(5, 0),
        Pair(6, 0)
    ),
    isDarkTheme: Boolean = false
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
            Spacer(Modifier.height(10.dp))
            ChartHeader("Pomodoro Activity", Icons.Filled.AvTimer, onFilterAction = {})
            Spacer(Modifier.height(10.dp))
            CustomView(chartPair, isDarkTheme)
        }
    }


}

@Composable
fun TaskChart(
    modifier: Modifier = Modifier,
    chartPair: List<Pair<Int, Int>> = listOf(),
    isDarkTheme: Boolean = false
) {

    val daysOfWeek = listOf("Mo", "Tu", "Wd", "Th", "Fr", "St", "Sn")

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
            Spacer(Modifier.height(10.dp))
            ChartHeader("Task Activity", Icons.Filled.AddChart, onFilterAction = {})
            Spacer(Modifier.height(10.dp))
            CustomView(chartPair, isDarkTheme)
        }
    }

}

@Composable
fun ChartHeader(title: String, icon: ImageVector, onFilterAction: (ChartFilterStatus) -> Unit) {
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
                imageVector = icon,
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
    isDarkTheme: Boolean = false
) {
    val activity = LocalContext.current as Activity
    val labelColor =
        if (isDarkTheme) Color.parseColor("#E2E2E6") else Color.parseColor("#1A1C1E")
    val barColor =
        if (isDarkTheme) Color.parseColor("#0062A0") else Color.parseColor("#0062A0")


    //max y in pairs
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
            RoundedBarChart(
                context
            )
        },
        update = { view ->
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication
            view.data = barData
            view.setRadius(30)
            view.xAxis.apply {
                valueFormatter = MyXAxisFormatter()
                position = XAxis.XAxisPosition.BOTTOM
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
                axisMaximum =
                    if (maxY != 0) (kotlin.math.ceil(maxY.toDouble() / 10) * 10).toFloat() else 10f
                this.axisLineColor = labelColor
                this.textColor = labelColor
                axisMinimum = 0f
            }
            view.setTouchEnabled(false)
            view.axisRight.isEnabled = false
            view.description = Description().also { it.text = "" }
            view.legend.form = Legend.LegendForm.NONE
            view.animateY(500)
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