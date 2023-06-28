package com.oguzhancetin.pomodoro.presentation.commonUI.Util

import android.content.Context
import android.util.TypedValue


/**
 * pomodoro -> Pomodoro
 */
fun String.UpperFirstChar(): String{
    return "${this[0].uppercase() + this.substring(1,this.length)}"
}
fun getThemeColor(context: Context, attrId: Int): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}

fun String.clearSufAndPrefix() = this.removePrefix("{").removeSuffix("}")