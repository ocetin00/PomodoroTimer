package com.oguzhancetin.pomodoro.presentation.common.util

sealed class Screen(val route: String,  val resourceId: String,val title:String) {
    object Report : Screen("report", "report","Status")
    object Setting : Screen("setting", "setting","Setting")
    object Main : Screen("main", "main","Pomodoro")
    object Task : Screen("main", "main","Pomodoro")
}