package com.oguzhancetin.pomodoro.presentation.common.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import  androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Created by ocetin00 on 9.01.2023
 */

@Preview
@Composable
fun ToggleTabPreview() {
    var selectedPageIndex by remember {
        mutableStateOf(1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
                .background(Color.White), horizontalArrangement = Arrangement.Center
        ) {
            ToggleTab(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.9f)
                    .clip(ShapeDefaults.ExtraLarge),
                onTabSelected = { index ->
                    selectedPageIndex = index
                },
                titleList = listOf("Tümü", "ConnectTruck", "Bağlı Değil"),
                tabItemTextStyle = TextStyle.Default.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
        var backgroundColor: Color = when (selectedPageIndex) {
            0 -> Color.Green
            1 -> Color.Magenta
            2 -> Color.Cyan
            else -> {
                Color.Green
            }
        }
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
                .background(backgroundColor)
        ) {

        }
    }
}

@Composable
fun ToggleTab(
    modifier: Modifier = Modifier,
    titleList: List<String>,
    initialSelectedTabIndex: Int = 0,
    tabItemTextStyle: TextStyle = TextStyle.Default,
    containerColor: Color = Color.Blue.copy(alpha = 0.5f),
    selectedColor: Color = Color.White,
    onTabSelected: (Int) -> Unit
) {
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {

        var currentTabIndex by remember { mutableStateOf(initialSelectedTabIndex) }

        TabRow(selectedTabIndex = currentTabIndex,
            modifier = modifier
                .fillMaxWidth(fraction = 0.9f)
                .clip(ShapeDefaults.ExtraLarge),
            containerColor = containerColor,
            indicator = {
                Box(
                    Modifier
                        .zIndex(-1f)
                        .tabIndicatorOffset(it[currentTabIndex])
                        .fillMaxSize()
                        .clip(ShapeDefaults.ExtraLarge)
                        .background(selectedColor)
                        .border(0f.dp, Color.Blue.copy(alpha = 0.5f), ShapeDefaults.ExtraLarge)
                )
            },
            divider = {}
        ) {

            val tabModifier = Modifier
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(70.dp))

            titleList.forEachIndexed { index, title ->
                val textColor =
                    if (index == currentTabIndex) containerColor else selectedColor
                Tab(
                    selectedContentColor = Color.Transparent,
                    modifier = tabModifier,
                    selected = false,
                    text = { Text(text = title, color = textColor, style = tabItemTextStyle) },
                    onClick = {
                        currentTabIndex = index
                        onTabSelected(index)
                    }
                )
            }
        }
    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}