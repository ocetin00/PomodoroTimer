package com.oguzhancetin.pomodoro.presentation.common

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.oguzhancetin.pomodoro.presentation.common.util.entryModelOf
import com.oguzhancetin.pomodoro.presentation.common.util.rememberChartStyle
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.LineComponent

@Preview()
@Composable
fun PomodoroAppChart(
    chartPair: List<Pair<Int, Int>> = listOf(
        Pair(0, 1),
        Pair(1, 3),
        Pair(2, 3),
        Pair(3, 2),
        Pair(4, 5),
        Pair(5, 1),
        Pair(6, 1),

    ),
    bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>? = null,
    startAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start>? = null,
) {


    val labelColor = MaterialTheme.colorScheme.onBackground
    val entityColor = MaterialTheme.colorScheme.primaryContainer

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ProvideChartStyle(rememberChartStyle(listOf(entityColor,labelColor))) {
            val defaultColumns = currentChartStyle.columnChart.columns
            Chart(
                chart = columnChart(
                    columns = remember(defaultColumns) {
                        defaultColumns.map { defaultColumn ->
                            LineComponent(
                                defaultColumn.color,
                                10f,
                                defaultColumn.shape
                            )
                        }
                    }
                ),
                model = entryModelOf(chartPair),
                startAxis = startAxisValueFormatter?.let {
                    startAxis(
                        valueFormatter = it,
                        label = rememberAxisLabel(),
                        maxLabelCount = 10,
                    )
                }

                ,
                bottomAxis = bottomAxisValueFormatter?.let {
                    bottomAxis(
                        valueFormatter = it,
                        label = rememberAxisLabel(),
                        guideline = LineComponent(
                            color = Color.TRANSPARENT,
                            thicknessDp = 0f
                        )
                    )
                })
        }
    }
}

@Composable
private fun rememberAxisLabel() = axisLabelComponent(
    color = MaterialTheme.colorScheme.onSurface,
)