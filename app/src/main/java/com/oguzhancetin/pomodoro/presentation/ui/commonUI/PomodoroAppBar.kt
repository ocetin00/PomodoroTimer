package com.oguzhancetin.pomodoro.presentation.ui.commonUI

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oguzhancetin.pomodoro.presentation.ui.theme.light_onRedBackground

/**
 * Created by ocetin00 on 11.01.2023
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    openDrawer: () -> Unit = {},
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    canNavigateBack: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState),
    navigateUp: () -> Unit = {},
    currentRoute: String
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        title = { Text(text = currentRoute, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                IconButton(onClick = openDrawer) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        }/*,
        actions = {
            IconButton(onClick = { *//* TODO: Open search *//* }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.menu),
                    tint = light_onSurfaceRed
                )
            }
        }*/,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    currentRoute: String
) {
    CenterAlignedTopAppBar(
        title = { Text(text = currentRoute, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        actions = {
            IconButton(onClick = {
                navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Cancel",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = modifier
    )
}