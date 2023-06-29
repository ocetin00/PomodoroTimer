package com.oguzhancetin.pomodoro.presentation.common

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PomodoroSwitch(checked: Boolean = false, onCheckedChange: (Boolean) -> Unit = {}) {
    Switch(
        checked = checked,
        onCheckedChange = { onCheckedChange(it) },

    )

}
