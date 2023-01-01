package com.oguzhancetin.pomodoro.screen.util

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MultipleToggleButton(modifier: Modifier = Modifier.height(25.dp)) {

    val active = 1

    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {


            //renderToggleItem("Pomodoro")
            //renderToggleItem("Short",true)
            //renderToggleItem("Long")
        }
    }
}

@Composable
fun renderToggleItem(text: String, isEnabled: Boolean = false) {
    if (isEnabled) {

        Surface(
            shape = MaterialTheme.shapes.large,
            color = Color.LightGray

        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(50.dp)
            ) {
                ToggleText(text)
            }

        }

    } else {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .width(50.dp)
        ) {
            ToggleText(text)
        }
    }

}

@Composable
fun ToggleText(text: String) {
    Text(text, fontSize = 10.sp)
}

//@Preview
@Composable
fun PreviewMultipleToggleButton() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            MultipleToggleButton()
        }
    }
}

@Preview
@Composable
fun PreviewTriStateToggle() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            TriStateToggle(Modifier.fillMaxWidth(0.9f))
        }
    }
}

@Composable
fun TriStateToggle(modifier: Modifier = Modifier) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val states = listOf(
        "Pomodoro",
        "Short",
        "Long",
    )
    var selectedOption by remember {
        mutableStateOf(states[1])
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,

        ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier
                .onSizeChanged {
                    size = it
                }
                .clip(shape = RoundedCornerShape(24.dp))
                .background(Color.LightGray)
        ) {
            states.forEach { text ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .then(
                            with(LocalDensity.current) {
                                Modifier.size(
                                    width = size.width.toDp()/3,
                                    height = 45.dp,
                                )
                            }
                        )
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable {
                            onSelectionChange(text)
                        }
                        .background(
                            if (text == selectedOption) {
                                Color.Magenta
                            } else {
                                Color.LightGray
                            }
                        )
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp,
                        )
                ) {
                    Text(
                        text = text,
                        color = Color.White

                    )
                }
            }
        }
    }
}