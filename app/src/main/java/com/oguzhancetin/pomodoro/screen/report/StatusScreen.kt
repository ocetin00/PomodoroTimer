package com.oguzhancetin.pomodoro.ui

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.screen.task.TasKAppBar
import com.oguzhancetin.pomodoro.screen.task.TaskScreenContent
import com.oguzhancetin.pomodoro.ui.theme.PomodoroTheme
import com.oguzhancetin.pomodoro.ui.theme.light_onSurfaceRed
import com.github.mikephil.charting.data.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreen(modifier: Modifier, onBack: () -> Unit) {

    PomodoroTheme() {
        Scaffold(
            topBar = {
                TasKAppBar(openDrawer = onBack)
            },
            modifier = modifier
        ) { innerPadding ->

            val contentModifier = Modifier
                .padding(innerPadding)
            StatusScreenContent(
               modifier = contentModifier
            )
        }
    }


}

@Composable
fun StatusScreenContent(modifier:Modifier = Modifier) {
    val entries = arrayListOf<Entry>()
    entries.add(Entry(1f,1f))
    entries.add(Entry(2f,2f))
    entries.add(Entry(3f,3f))
    entries.add(Entry(4f,1f))
    entries.add(Entry(5f,5f))
    entries.add(Entry(6f,1f))
    entries.add(Entry(7f,8f))
    val lineDataSet = LineDataSet(entries,"task_items")
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
            }
        }, modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
            update = {
                it.invalidate()
            })
    }
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
        title = { Text(text = title, color = light_onSurfaceRed) },
        navigationIcon = {
            IconButton(onClick = onBack) {
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

