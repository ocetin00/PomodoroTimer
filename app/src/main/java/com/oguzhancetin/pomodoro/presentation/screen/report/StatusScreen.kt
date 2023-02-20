package com.oguzhancetin.pomodoro.presentation.screen.report

import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.presentation.ui.theme.light_onRedBackground
import com.oguzhancetin.pomodoro.common.util.chart.XAxisValueFormatter
import com.oguzhancetin.pomodoro.common.util.removeDetails
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import java.util.*

@Composable
fun StatusScreen(
    modifier: Modifier,
    onBack: () -> Unit,
    viewModel: ReportViewModal = hiltViewModel()
) {

    val reportUIState by viewModel.reportUIState.collectAsState()

    reportUIState.errorMessage?.let{

    }

    if(reportUIState.isLoading){

    }else{
        StatusScreenContent(
            modifier = modifier,
            pomodoroList = reportUIState.pomodoroList
        )
    }





}


@Preview
@Composable
fun StatusScreenContent(
    modifier: Modifier = Modifier,
    pomodoroList: List<Pomodoro> = listOf()
) {


    val entries = convertEntryList(pomodoroList)

    val lineDataSet = LineDataSet(entries, "task_items")


    lineDataSet.setDrawValues(false);
    lineDataSet.setDrawCircles(false);
    lineDataSet.setDrawFilled(true);
    lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER;
    lineDataSet.cubicIntensity = 0.2f;
    val lineData = LineData(lineDataSet)

    Column(
        modifier = modifier
    ) {
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

fun convertEntryList(sortedTaskItems: List<Pomodoro>): List<Entry> {

    val calendar = Calendar.getInstance()
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

