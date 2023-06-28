package com.oguzhancetin.pomodoro.presentation.commonUI

import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
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
