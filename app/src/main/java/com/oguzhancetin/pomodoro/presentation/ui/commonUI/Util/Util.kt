package com.oguzhancetin.pomodoro.presentation.ui.commonUI.Util


/**
 * pomodoro -> Pomodoro
 */
fun String.UpperFirstChar(): String{
    return "${this[0].uppercase() + this.substring(1,this.length)}"
}