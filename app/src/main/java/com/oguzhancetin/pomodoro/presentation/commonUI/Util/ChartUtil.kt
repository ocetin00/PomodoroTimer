package com.oguzhancetin.pomodoro.presentation.commonUI.Util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.oguzhancetin.pomodoro.core.util.removeDetails
import com.oguzhancetin.pomodoro.domain.model.Doneable
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import java.util.Calendar
import java.util.TimeZone

fun convertListToXYPairs(sortedTaskItems: List<Doneable>): List<Pair<Int, Int>> {

    val calendar = Calendar.getInstance(TimeZone.getDefault())
    val daysValue = mutableMapOf<Long, Int>()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    calendar.removeDetails()

    //add week days
    repeat(7) {
        daysValue[calendar.timeInMillis] = 0
        calendar.add(Calendar.DATE, 1)
    }

    //sorted to obtain which day and added to days hasMap
    sortedTaskItems.forEach { item ->
        daysValue.keys.forEach { key ->
            if (item.doneDate == key) {
                val currentValue = daysValue[key] ?: 0
                daysValue[key] = currentValue + 1
            }
        }
    }

    //convert to pair
    var tempCounter = 0
    val dayToValue = mutableListOf<Pair<Int, Int>>()
    daysValue.values.forEach {
        dayToValue.add(Pair(tempCounter, it))
        tempCounter++
    }
    return dayToValue
}

@Composable
internal fun rememberChartStyle(
    columnChartColors: List<androidx.compose.ui.graphics.Color>,
    lineChartColors: List<androidx.compose.ui.graphics.Color>
): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return remember(columnChartColors, lineChartColors, isSystemInDarkTheme) {
        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = Color(defaultColors.axisLabelColor),
                axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
                axisLineColor = Color.Transparent,
            ),
            ChartStyle.ColumnChart(
                columnChartColors.map { columnChartColor ->
                    LineComponent(
                        columnChartColor.toArgb(),
                        DefaultDimens.COLUMN_WIDTH,
                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
                    )
                },
            ),
            ChartStyle.LineChart(
                lineChartColors.map { lineChartColor ->
                    LineChart.LineSpec(
                        lineColor = lineChartColor.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                                ),
                            ),
                        ),
                    )
                },
            ),
            ChartStyle.Marker(),
            Color(defaultColors.elevationOverlayColor),
        )
    }
}

@Composable
internal fun rememberChartStyle(chartColors: List<androidx.compose.ui.graphics.Color>) =
    rememberChartStyle(columnChartColors = chartColors, lineChartColors = chartColors)


public fun entryModelOf(entries: List<Pair<Number, Number>>): ChartEntryModel =
    entries
        .map { (x, y) -> entryOf(x.toFloat(), y.toFloat()) }
        .let { entryList -> ChartEntryModelProducer(listOf(entryList)) }
        .getModel()