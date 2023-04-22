package com.oguzhancetin.pomodoro.presentation.ui.commonUI

import android.graphics.Color
import android.icu.text.NumberFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.mikephil.charting.data.Entry
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
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.PercentageFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.formatter.DecimalFormatValueFormatter
import java.text.DecimalFormat

@Composable
fun PomodoroAppChart(
    chartPair: List<Pair<Int, Int>> = listOf(
        Pair(0, 1),
        Pair(1, 3),
        Pair(2, 3),
        Pair(3, 2),
        Pair(4, 9),
        Pair(5, 1),
        Pair(6, 1)
    ),
    bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>?,
) {


    val labelColor = MaterialTheme.colorScheme.onBackground
    val entityColor = MaterialTheme.colorScheme.primaryContainer

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ProvideChartStyle(rememberChartStyle(listOf(entityColor))) {
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
                startAxis =
                    startAxis(
                    )
                ,
                bottomAxis = bottomAxisValueFormatter?.let {
                    bottomAxis(
                        valueFormatter = it,
                        guideline = LineComponent(
                            color = Color.TRANSPARENT,
                            thicknessDp = 0f
                        )
                    )
                })
        }
    }
}