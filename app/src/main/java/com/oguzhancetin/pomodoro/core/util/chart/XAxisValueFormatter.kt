package com.oguzhancetin.pomodoro.core.util.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class XAxisValueFormatter : ValueFormatter() {
    private val days = arrayOf("Mo", "Tu", "Wed", "Th", "Fr", "Sa","Sun")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}