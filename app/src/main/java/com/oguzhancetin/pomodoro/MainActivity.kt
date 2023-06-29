package com.oguzhancetin.pomodoro

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oguzhancetin.pomodoro.core.preference.IS_DARK_MODE_KEY
import com.oguzhancetin.pomodoro.core.preference.dataStore
import com.oguzhancetin.pomodoro.core.database.PomodoroDatabase
import com.oguzhancetin.pomodoro.presentation.PomodoroApp
import com.oguzhancetin.pomodoro.presentation.theme.PomodoroTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var appDb: PomodoroDatabase

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        /*

        private const val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
        private const val IGNORE_BATTERY_OPTIMIZATIONS =
    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                  val intent = Intent()
                  val packageName = packageName
                  val pm = getSystemService(POWER_SERVICE) as PowerManager
                  if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                      intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                      intent.data = Uri.parse("package:$packageName")
                      startActivity(intent)
                  }
              }
           // Register the contract in your fragment/activity and handle the result
              val permissionRequest =
                  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

                  }

              if (ContextCompat.checkSelfPermission(
                      this,
                      POST_NOTIFICATIONS,
                  ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                      this,
                      IGNORE_BATTERY_OPTIMIZATIONS,
                  ) == PackageManager.PERMISSION_GRANTED
              ) {

              } else {
                  permissionRequest.launch(arrayOf(POST_NOTIFICATIONS, IGNORE_BATTERY_OPTIMIZATIONS))
              }*/

        // Sets up permissions request launcher.
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {

            }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val isDarkMode = shouldUseDarkTheme(uiState)


            // Update the dark content of the system bars to match the theme
            DisposableEffect(systemUiController, isDarkMode) {
                systemUiController.systemBarsDarkContentEnabled = !isDarkMode
                @ColorRes val backGroundColor =
                    if (isDarkMode) R.color.colorPrimaryDark else R.color.colorPrimary
                window.decorView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        backGroundColor
                    )
                )
                onDispose {}
            }
            PomodoroTheme(darkTheme = isDarkMode) {
                PomodoroApp()
            }

        }

    }
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val context: Application,
) : ViewModel() {


    val enableDarkMode = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: false
        }

    val uiState: StateFlow<MainActivityUiState> = enableDarkMode.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val isDarkModel: Boolean) : MainActivityUiState
}


@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> uiState.isDarkModel
}

