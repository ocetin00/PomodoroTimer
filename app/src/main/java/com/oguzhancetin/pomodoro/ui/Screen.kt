package com.oguzhancetin.pomodoro.ui

import androidx.annotation.StringRes
import com.oguzhancetin.pomodoro.R

sealed class Screen(val route: String,  val resourceId: String,val title:String) {
    object Report : Screen("report", "report","Status")
    object Setting : Screen("setting", "setting","Setting")
    object Main : Screen("main", "main","Pomodoro")
    object Task : Screen("main", "main","Pomodoro")
}