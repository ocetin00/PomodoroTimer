package com.oguzhancetin.pomodoro.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.presentation.PomodoroDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToMain: () -> Unit,
    navigateToSetting: () -> Unit,
    navigateToStatus: () -> Unit,
    closeDrawer: () -> Unit,
    navigateToTask: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(modifier, drawerContentColor = MaterialTheme.colorScheme.onSurface) {
        DrawerSheetHeader(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
        )
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { },
            label = { Text(stringResource(R.string.pomodoro_title)) },
            selected = currentRoute == PomodoroDestinations.MAIN_ROUTE,
            onClick = {
                closeDrawer();navigateToMain()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { },
            label = { Text(stringResource(R.string.settings_title)) },
            selected = currentRoute == PomodoroDestinations.SETTING_ROUTE,
            onClick = { navigateToSetting() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { },
            label = { Text(stringResource(R.string.status_title)) },
            selected = currentRoute == PomodoroDestinations.STATUS_ROUTE,
            onClick = { navigateToStatus() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { },
            label = { Text(stringResource(R.string.Task)) },
            selected = currentRoute == PomodoroDestinations.TASK_ROUTE,
            onClick = { navigateToTask() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
fun DrawerSheetHeader(modifier: Modifier = Modifier) {
    Row(modifier) {
        Image(
            painterResource(R.drawable.pomodoro_app_icon),
            "content description",
            Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text("Pomodoro")
    }
}