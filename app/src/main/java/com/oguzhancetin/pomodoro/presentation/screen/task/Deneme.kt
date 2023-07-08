package com.oguzhancetin.pomodoro.presentation.screen.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun Deneme() {
    var textValue by remember { mutableStateOf("") }
    Column(
        Modifier
            .fillMaxHeight()
            .background(Color.Yellow)

    ) {
        Column(
            Modifier
                .weight(1f)
                .imePadding(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            TextField(value = textValue, onValueChange = { textValue = it })
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Button")
            }
        }


    }


}