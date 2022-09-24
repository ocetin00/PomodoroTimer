package com.oguzhancetin.pomodoro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.ui.theme.light_onSurfaceRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToMain: () -> Unit,
    navigateToSetting: () -> Unit,
    navigateToStatus: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
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
            onClick = { navigateToMain();closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { },
            label = { Text(stringResource(R.string.settings_title)) },
            selected = currentRoute == PomodoroDestinations.SETTING_ROUTE,
            onClick = { navigateToSetting();closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { },
            label = { Text(stringResource(R.string.status_title)) },
            selected = currentRoute == PomodoroDestinations.STATUS_ROUTE,
            onClick = { navigateToStatus();closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
fun DrawerSheetHeader(modifier: Modifier = Modifier) {
    Row(modifier) {
        Icon(imageVector = Icons.Filled.Timelapse, contentDescription = "pomodoro_icon")
        Spacer(Modifier.width(8.dp))
        Text("Pomodoro")
    }
}