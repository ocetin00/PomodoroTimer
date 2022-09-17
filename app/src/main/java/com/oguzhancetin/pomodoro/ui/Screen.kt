package com.oguzhancetin.pomodoro.ui

import androidx.annotation.StringRes
import com.oguzhancetin.pomodoro.R

sealed class Screen(val route: String,  val resourceId: String) {
    object Report : Screen("report", "report")
    object Setting : Screen("setting", "setting")
    object Main : Screen("main", "main")
}