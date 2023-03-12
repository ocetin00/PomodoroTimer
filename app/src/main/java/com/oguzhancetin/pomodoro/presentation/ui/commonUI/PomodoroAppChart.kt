package com.oguzhancetin.pomodoro.presentation.ui.commonUI

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.entryModelOf
import com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util.rememberChartStyle
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.LineComponent

@Composable
fun PomodoroAppChart(
    chartPair: List<Pair<Int, Int>>,
    bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
) {
    val labelColor = MaterialTheme.colorScheme.onBackground
    val entityColor = MaterialTheme.colorScheme.primaryContainer
    val chartColors = listOf(entityColor)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ProvideChartStyle(rememberChartStyle(chartColors)) {
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
                startAxis = startAxis(maxLabelCount = 5),
                bottomAxis = bottomAxis(
                    valueFormatter = bottomAxisValueFormatter,
                    guideline = LineComponent(
                        color = Color.TRANSPARENT,
                        thicknessDp = 0f
                    )
                ))
        }
    }
}