package com.oguzhancetin.pomodoro.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingScreen(modifier: Modifier){
    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxSize()) {
        Text(text = "setting ")
    }
}