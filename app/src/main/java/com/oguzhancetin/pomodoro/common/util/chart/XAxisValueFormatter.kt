package com.oguzhancetin.pomodoro.common.util.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class XAxisValueFormatter : ValueFormatter() {
    private val days = arrayOf("Su","Mo", "Tu", "Wed", "Th", "Fr", "Sa")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}