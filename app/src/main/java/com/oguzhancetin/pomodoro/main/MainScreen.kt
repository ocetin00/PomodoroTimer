package com.oguzhancetin.pomodoro.main

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.ui.StatelessTimer
import com.oguzhancetin.pomodoro.ui.theme.*
import com.oguzhancetin.pomodoro.util.Times
import java.sql.Time



@SuppressLint("EnqueueWork")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val currentTime = viewModel.currentTime
    Surface(
        color = SurfaceRed
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            TopButtons(onClickButton = {viewModel.updateCurrentTime(it)}, currentTimeType = currentTime)
            Spacer(modifier = Modifier.height(35.dp))
            TimerBody(viewModel)
            Spacer(modifier = Modifier.height(20.dp))
            FavoriteTasks()
        }
    }
}

@Composable
private fun TopButtons(onClickButton:(Times)-> Unit,currentTimeType: Times = Times.PomodoroTime()) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            border = BorderStroke(1.dp, light_SurfaceRedContainer),
            colors = if(currentTimeType is Times.LongTime){
                ButtonDefaults.outlinedButtonColors(containerColor = light_onSurfaceRed, contentColor = MaterialTheme.colorScheme.primary )
            } else {
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.LongTime())
            }) {
            Text("Long Break")
        }
        OutlinedButton(
            border = BorderStroke(1.dp, light_SurfaceRedContainer),
            colors = if(currentTimeType is Times.Short) {
                ButtonDefaults.outlinedButtonColors(containerColor = light_onSurfaceRed, contentColor = MaterialTheme.colorScheme.primary )
            } else{
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.Short())
            }) {
            Text("Short Break")
        }
        OutlinedButton(
            border = BorderStroke(1.dp, light_SurfaceRedContainer),
            colors = if(currentTimeType is Times.PomodoroTime) {
                ButtonDefaults.outlinedButtonColors(containerColor = light_onSurfaceRed, contentColor = MaterialTheme.colorScheme.primary )
            } else{
                ButtonDefaults.outlinedButtonColors()
            },
            onClick = {
                onClickButton(Times.PomodoroTime())
            }) {
            Text("Pomodoro")
        }
    }
}

@Composable
fun TimerBody(viewModel: MainViewModel) {
    val time = viewModel.currentTime
    if (viewModel.timerIsRunning) {
        val workInfo = viewModel.workInfo?.observeAsState()
        val left = workInfo?.value?.progress?.getFloat(
            "Left",
            viewModel.currentTime.getCurrentPercentage()
        )
        left?.let {
            viewModel.updateCurrent(it)
        }
    }
    Box() {
        StatelessTimer(
            value = time.getCurrentPercentage(),
            time = time.toString(),
            textColor = light_onSurfaceRed,
            handleColor = Color.Green,
            inactiveBarColor = Color.White,
            activeBarColor = md_theme_light_tertiary,
            modifier = Modifier.size(230.dp)
        )
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
        ) {

            IconButton(modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(Color.White)
                 ,onClick = { viewModel.pauseOrPlayTimer() }) {
                Icon(
                    if (viewModel.timerIsRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)

                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            IconButton(modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(Color.White),onClick = { viewModel.restart() }) {
                Icon(
                    Icons.Filled.Refresh, "",
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp)
                )
            }

        }
    }
}


@Composable
fun FavoriteTasks() {

    Column(Modifier.fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally) {
        repeat(3) {
            Spacer(modifier = Modifier.height(8.dp))
            ImportantTask()
        }
    }
}


@Composable
fun ImportantTask(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(Color.White.copy(0.2f)),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
                painter = painterResource(id = R.drawable.dot),
                contentDescription = "Dot"
            )
            Text("Deneme task")
            Icon(imageVector = Icons.Filled.Star, contentDescription = "Dot",tint = md_theme_light_tertiary)
        }
    }

}

@Preview
@Composable
fun Deneme() {
    val activeTime by remember{ mutableStateOf<Times>(Times.Short())}
    Surface(
        color = SurfaceRed
    ){
        Row{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    border = BorderStroke(1.dp, light_SurfaceRedContainer),
                    colors = if(activeTime is Times.LongTime){
                        ButtonDefaults.outlinedButtonColors(containerColor = light_onSurfaceRed, contentColor = MaterialTheme.colorScheme.primary )
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    onClick = {

                    }) {
                    Text("Long Break")
                }
                OutlinedButton(
                    border = BorderStroke(1.dp, light_SurfaceRedContainer),
                    colors = if(activeTime is Times.Short) {
                        ButtonDefaults.outlinedButtonColors(containerColor = light_onSurfaceRed, contentColor = MaterialTheme.colorScheme.primary )
                    } else{
                        ButtonDefaults.outlinedButtonColors()
                    },
                    onClick = {

                    }) {
                    Text("Short Break", )
                }
                OutlinedButton(
                    border = BorderStroke(1.dp, light_SurfaceRedContainer),
                    colors = if(activeTime is Times.PomodoroTime) {
                        ButtonDefaults.outlinedButtonColors(containerColor = light_onSurfaceRed, contentColor = MaterialTheme.colorScheme.primary )
                    } else{
                        ButtonDefaults.outlinedButtonColors()
                    },
                    onClick = {

                    }) {
                    Text("Pomodoro", )
                }
            }
        }
    }


}



